package com.example.inventory.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.sale.SaleCreateRequest;
import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleResponse;
import com.example.inventory.dto.sale.SaleUpdateRequest;
import com.example.inventory.entity.Firm;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Sale;
import com.example.inventory.entity.SaleItem;
import com.example.inventory.entity.User;
import com.example.inventory.enums.Status;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.mapper.SaleItemMapper;
import com.example.inventory.mapper.SaleMapper;
import com.example.inventory.repository.FirmRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.SaleRepository;
import com.example.inventory.service.SaleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

	private final SaleRepository saleRepository;
	private final SaleMapper saleMapper;
	private final FirmRepository firmRepository;
	private final ProductRepository productRepository;
	private final SaleItemMapper saleItemMapper;

	@Override
	public SaleResponse createSale(SaleCreateRequest request) {
		User currentUser = getCurrentUser();
		Firm firm = findFirmById(request.getFirmId());

		Sale sale = buildSale(request, currentUser, firm);
		Sale savedSale = saleRepository.save(sale);

		return saleMapper.toResponse(savedSale);
	}

	@Override
	public SaleResponse getSaleById(Long id) {
		return saleRepository.findById(id).map(saleMapper::toResponse).orElse(null);
	}

	@Override
	public List<SaleResponse> getAllSales() {
		return saleRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(saleMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public SaleResponse updateSale(Long id, SaleUpdateRequest request) {
		// Güncellenecek satışı veritabanından bul
		Sale sale = saleRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Sale not found with id: " + id));

		// Request geçerli mi kontrol et (ürün listesi boş olamaz)
		validateUpdateRequest(request);

		// Güncel toplam tutar hesaplaması için başlangıç değeri
		BigDecimal totalAmount = BigDecimal.ZERO;

		// Mevcut saleItem'ları map'e al: productId -> SaleItem
		Map<Long, SaleItem> existingItemsMap = sale.getItems().stream()
				.collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));

		// Request’teki her bir ürün için işlemleri yap
		for (var itemRequest : request.getItems()) {
			Product product = findProduct(itemRequest.getProductId());

			if (existingItemsMap.containsKey(itemRequest.getProductId())) {
				// Var olan ürünü güncelle
				SaleItem saleItem = existingItemsMap.get(itemRequest.getProductId());

				int quantityDiff = itemRequest.getQuantity() - saleItem.getQuantity();
				adjustProductStock(product, quantityDiff);
				updateSaleItem(saleItem, product, itemRequest.getQuantity());

				// İşlem tamamlandı → map'ten çıkar
				existingItemsMap.remove(itemRequest.getProductId());
			} else {
				// Yeni ürün ekle
				if (product.getStockQuantity() < itemRequest.getQuantity()) {
					throw new BusinessException("Not enough stock for product: " + product.getName());
				}
				product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
				productRepository.save(product);

				SaleItem newItem = saleItemMapper.toEntity(itemRequest, sale, product);
				newItem.setSale(sale);
				sale.getItems().add(newItem);
			}
		}

		// Request'te olmayan mevcut ürünler → sil ve stoğa iade et
		for (SaleItem toRemove : existingItemsMap.values()) {
			Product product = toRemove.getProduct();
			product.setStockQuantity(product.getStockQuantity() + toRemove.getQuantity());
			productRepository.save(product);

			sale.getItems().remove(toRemove);
		}

		// Toplam tutarı güncelle
		totalAmount = sale.getItems().stream().map(SaleItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		sale.setTotalAmount(totalAmount);

		// Güncellenmiş satışı kaydet
		Sale updated = saleRepository.save(sale);

		//  Response döndür
		return saleMapper.toResponse(updated);
	}

	// -------------------- Update için yardımcı metodlar--------------------------

	// Request doğrulama — ürün listesi boş olamaz
	private void validateUpdateRequest(SaleUpdateRequest request) {
		if (request.getItems() == null || request.getItems().isEmpty()) {
			throw new BusinessException("At least one item must be provided for update.");
		}
	}
	// Veritabanından aktif ürünü bulur
	private Product findProduct(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new BusinessException("Product not found with id: " + productId));
	}
	// Ürün stok miktarını günceller
	// Eğer quantityDiff pozitifse, satış miktarı artmış demektir — stok düşülür
	// Eğer negatifse, satış miktarı azalmış demektir — stok geri eklenir
	private void adjustProductStock(Product product, int quantityDiff) {
		if (quantityDiff == 0) {
			return; // Miktar değişmemişse işlem yapma
		}

		if (quantityDiff > 0) {
			// Satış miktarı arttı — yeterli stok var mı kontrol et
			ensureSufficientStock(product, quantityDiff);
			product.setStockQuantity(product.getStockQuantity() - quantityDiff);
		} else {
			// Satış miktarı azaldı — stoğa iade et
			product.setStockQuantity(product.getStockQuantity() + Math.abs(quantityDiff));
		}
		product.setStatus(product.getStockQuantity() > 0 ? Status.ACTIVE : Status.INACTIVE);
		// Güncel stok miktarını kaydet
		productRepository.save(product);
	}

	// Stok miktarını kontrol eder, yetersizse hata fırlatır
	private void ensureSufficientStock(Product product, int requiredAmount) {
		if (product.getStockQuantity() < requiredAmount) {
			throw new BusinessException("Not enough stock for product: " + product.getName());
		}
	}

	// Satış kalemini günceller (miktar ve toplam fiyat)
	private void updateSaleItem(SaleItem saleItem, Product product, int newQuantity) {
		saleItem.setQuantity(newQuantity);
		saleItem.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
	}

	// ----------------------------Private helper methods---------------------------------

	private User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private Firm findFirmById(Long firmId) {
		return firmRepository.findById(firmId)
				.orElseThrow(() -> new RuntimeException("Firm not found with id: " + firmId));
	}

	private Sale buildSale(SaleCreateRequest request, User user, Firm firm) {
		Sale sale = saleMapper.toEntity(request); // İstek verisini Sale nesnesine çevir
		sale.setUser(user); // Satışı yapan kullanıcıyı set et
		sale.setFirm(firm); // Firmayı set et

		List<SaleItem> saleItems = createSaleItems(request, sale); // Satış oluştur
		sale.setItems(saleItems); // verileri satışa ekle
		sale.setTotalAmount(calculateTotalAmount(saleItems)); // Toplam tutarı hesapla

		return sale;
	}

	// Satış isteği (SaleCreateRequest) içindeki birden fazla ürün kalemini (item) alır ve her biri için SaleItem nesnesi oluşturur.
	private List<SaleItem> createSaleItems(SaleCreateRequest request, Sale sale) {
		if (request.getItems() == null || request.getItems().isEmpty()) {
			return Collections.emptyList();
		}

		return request.getItems().stream().map(itemDto -> createSaleItem(itemDto, sale)).collect(Collectors.toList());
	}
    // Tek bir satış kalemini (yani bir ürün satırını) oluşturur ve stok güncellemesini yapar.
	private SaleItem createSaleItem(SaleItemCreateRequest request, Sale sale) {
		Product product = productRepository.findByIdAndDeletedFalse(request.getProductId())
				.orElseThrow(() -> new BusinessException("Ürün bulunamadı: ID=" + request.getProductId()));

		if (product.getStatus() == Status.INACTIVE) {
			throw new BusinessException("Bu ürün satışa kapalı (inactive): " + product.getName());
		}
		if (product.getStockQuantity() < request.getQuantity()) {
			throw new BusinessException("Yetersiz stok: " + product.getName());
		}
		// stok miktarını düş
		product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
		product.setStatus(product.getStockQuantity() > 0 ? Status.ACTIVE : Status.INACTIVE);
		productRepository.save(product);
		SaleItem saleItem = saleItemMapper.toEntity(request, sale, product);
		saleItem.setSale(sale);
		return saleItem;
	}

	private BigDecimal calculateTotalAmount(List<SaleItem> saleItems) {
		return saleItems.stream().map(SaleItem::getLineTotal) // Her ürünün toplam tutarını al
				.reduce(BigDecimal.ZERO, BigDecimal::add); // Hepsini topla
	}
}
