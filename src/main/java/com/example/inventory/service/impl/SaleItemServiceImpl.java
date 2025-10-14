package com.example.inventory.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Sale;
import com.example.inventory.entity.SaleItem;
import com.example.inventory.enums.Status;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.mapper.SaleItemMapper;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.SaleItemRepository;
import com.example.inventory.repository.SaleRepository;
import com.example.inventory.service.SaleItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleItemServiceImpl implements SaleItemService {

	private final SaleItemRepository saleItemRepository;
	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	private final SaleItemMapper saleItemMapper;

	@Override
	public SaleItemResponse createSaleItem(Long saleId, SaleItemCreateRequest request) {
		// Satışı getir
		Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));

		// Ürünü getir
		/*
		 * Product product = productRepository.findById(request.getProductId())
		 * .orElseThrow(() -> new RuntimeException("Product not found"));
		 */
		// Ürün seçimi: ID, barkod veya isim ile
		Product product = findProduct(request);

		// Mapper ile SaleItem oluştur
		SaleItem saleItem = saleItemMapper.toEntity(request, sale, product);

		// Kaydet
		SaleItem saved = saleItemRepository.save(saleItem);

		return saleItemMapper.toResponse(saved);
	}

	@Override
	public List<SaleItemResponse> getSaleItemsBySaleId(Long saleId) {
		List<SaleItem> items = saleItemRepository.findBySaleId(saleId);
		return items.stream().map(saleItemMapper::toResponse).collect(Collectors.toList());
	}

	private Product findProduct(SaleItemCreateRequest request) {
		Product product = Optional.ofNullable(request.getProductId())
				.map(id -> productRepository.findByIdAndDeletedFalse(id)
						.orElseThrow(() -> new RuntimeException("Product not found by ID")))
				.or(() -> Optional.ofNullable(request.getBarcode()).filter(b -> !b.isEmpty())
						.map(b -> productRepository.findByBarcodeAndDeletedFalse(b)
								.orElseThrow(() -> new RuntimeException("Product not found by barcode"))))
				.or(() -> Optional.ofNullable(request.getName()).filter(n -> !n.isEmpty())
						.map(n -> productRepository.findByNameAndDeletedFalse(n)
								.orElseThrow(() -> new RuntimeException("Product not found by name"))))
				.orElseThrow(() -> new RuntimeException("Product identifier (ID, barcode, or name) is required"));

		if (product.getStatus() == Status.INACTIVE) {
			throw new BusinessException("Bu ürün satışa kapalı (inactive)");
		}

		return product;
	}

}
