package com.example.inventory.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.product.ProductCreateRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.dto.product.ProductStockReport;
import com.example.inventory.dto.product.ProductUpdateRequest;
import com.example.inventory.entity.Category;
import com.example.inventory.entity.Product;
import com.example.inventory.enums.Status;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.exception.ValidationException;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;

	@Override
	public ProductResponse createProduct(ProductCreateRequest request) {
		validateBarcodeUnique(request.getBarcode());

		Product product = productMapper.toEntity(request);
		// product.setDeleted(false); // yeni ürün aktif
	    product.setStatus(request.getStockQuantity() != null && request.getStockQuantity() > 0
	            ? Status.ACTIVE
	            : Status.INACTIVE);
		setCategoryIfPresent(product, request.getCategoryId());

		return productMapper.toResponse(productRepository.save(product));
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		Product existing = findActiveProductById(id);
		productMapper.updateEntityFromDto(request, existing);
		setCategoryIfPresent(existing, request.getCategoryId());
		  if (request.getStockQuantity() != null) {
		        existing.setStatus(request.getStockQuantity() > 0 ? Status.ACTIVE : Status.INACTIVE);
		    }

		return productMapper.toResponse(productRepository.save(existing));
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = findActiveProductById(id);
		product.setDeleted(true); // soft delete
		productRepository.save(product);
	}
	@Override
	public List<ProductResponse> searchProducts(String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        throw new ValidationException("Arama terimi boş olamaz.");
	    }

	    List<Product> products = productRepository
	            .findByDeletedFalseAndNameContainingIgnoreCaseOrBarcodeContainingIgnoreCase(keyword, keyword);

	    if (products.isEmpty()) {
	        throw new ResourceNotFoundException("Arama kriterine uygun ürün bulunamadı: " + keyword);
	    }

	    return products.stream()
	            .map(productMapper::toResponse)
	            .toList();
	}

	@Override
	public ProductResponse getProductById(Long id) {
		return productMapper.toResponse(findActiveProductById(id));
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id")).stream().map(productMapper::toResponse).toList();
	}

	@Override
	public List<ProductStockReport> getStockReport() {
		return productRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id")).stream().map(productMapper::toStockReport).toList();
	}

	// ------------------- Private Helper Methods -------------------

	private void validateBarcodeUnique(String barcode) {
		if (productRepository.existsByBarcodeAndDeletedFalse(barcode)) {
			throw new ValidationException("Barcode already exists: " + barcode);
		}
	}

	private void setCategoryIfPresent(Product product, Long categoryId) {
		if (categoryId == null) {
			product.setCategory(null);
			return;
		}

		Category category = categoryRepository.findByIdAndDeletedFalse(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
		product.setCategory(category);
	}

	private Product findActiveProductById(Long id) {
		return productRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	// ------------------- Stock Rapor Excel-------------------
	@Override
	public byte[] generateStockReportExcel() {

		List<ProductStockReport> report = getStockReport();
        // Workbook (Excel dosyası) ve ByteArrayOutputStream oluşturuluyor
		// try-with-resources kullanıldığı için işlem bittiğinde otomatik kapanır
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			// Yeni bir Excel sayfası (sheet) oluşturuluyor
			Sheet sheet = workbook.createSheet("Stock Report");

			// Ürünlerin stok durumuna göre (LOW, MEDIUM, HIGH) hücre renk stilleri oluşturuluyor
			Map<String, CellStyle> statusStyles = createStatusStyles(workbook);

			// Başlık satırı oluşturuluyor ("ID", "Name", "Barcode" vs.)
			createHeaderRow(sheet);

			// Veriler satır satır ekleniyor ve toplam stok miktarı hesaplanıyor
			int totalStock = populateDataRows(sheet, report, statusStyles);

			// En alta "TOTAL STOCK" satırı ekleniyor
			createTotalRow(sheet, totalStock);

			// Sütun genişlikleri otomatik olarak ayarlanıyor
			autoSizeColumns(sheet, 7);
            // Excel dosyası hafızadaki "out" akışına yazılıyor
			workbook.write(out);
			// Byte dizisine çevrilip controller’a return ediliyor
			return out.toByteArray();

		} catch (Exception e) {
			throw new BusinessException("Rapor oluşturulurken bir hata meydana geldi.", e);
		}
	}

	// ------------------- Excel Helper Methods -------------------

	private Map<String, CellStyle> createStatusStyles(Workbook workbook) {
		// Renkli hücre stillerini tutacak bir Map oluşturuluyor
		Map<String, CellStyle> styles = new HashMap<>();
		// LOW → kırmızı, MEDIUM → sarı, HIGH → yeşil renk veriliyor
		styles.put("LOW", createCellStyle(workbook, IndexedColors.RED));
		styles.put("MEDIUM", createCellStyle(workbook, IndexedColors.YELLOW));
		styles.put("HIGH", createCellStyle(workbook, IndexedColors.GREEN));
		return styles;
	}

	private CellStyle createCellStyle(Workbook workbook, IndexedColors color) {
	    // Hücre stili oluşturuluyor
		CellStyle style = workbook.createCellStyle();
		 // Arka plan rengi ayarlanıyor
		style.setFillForegroundColor(color.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	private void createHeaderRow(Sheet sheet) {
		 // İlk satır (0. index) başlık satırı olarak oluşturuluyor
		Row header = sheet.createRow(0);
		 // Başlık isimleri belirleniyor
		String[] columns = { "ID", "Name", "Barcode", "Category", "Stock Quantity", "Stock Status", "Unit Price" };
		// Her başlık hücresine isim yazılıyor
		for (int i = 0; i < columns.length; i++) {
			header.createCell(i).setCellValue(columns[i]);
		}
	}

	private int populateDataRows(Sheet sheet, List<ProductStockReport> report, Map<String, CellStyle> styles) {
	    int rowNum = 1;       // Başlıktan sonra veri 1. satırdan başlar
	    int totalStock = 0;   // Toplam stok sayısını tutmak için sayaç

	    // Her ürün için bir satır oluşturuluyor
		for (ProductStockReport p : report) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(p.getId());
			row.createCell(1).setCellValue(p.getName());
			row.createCell(2).setCellValue(p.getBarcode());
			row.createCell(3).setCellValue(p.getCategoryName() != null ? p.getCategoryName() : "");
			row.createCell(4).setCellValue(p.getStockQuantity());
			 // Toplam stoğa ekleniyor
	        totalStock += p.getStockQuantity();

	        // Stok durumu hücresi (LOW, MEDIUM, HIGH)
	        Cell stockStatusCell = row.createCell(5);
	        stockStatusCell.setCellValue(p.getStockStatus());

	        // Hücreye uygun renk stili atanıyor
	        CellStyle style = styles.getOrDefault(p.getStockStatus(), null);
	        if (style != null) {
	            stockStatusCell.setCellStyle(style);
	        }

	        // Fiyat bilgisi hücresine yazılıyor
	        row.createCell(6).setCellValue(p.getUnitPrice().doubleValue());
	    }
	     // Tüm stok toplamı geri döndürülüyor
		return totalStock;
	}

	private void createTotalRow(Sheet sheet, int totalStock) {
		// Son satırın altına yeni bir satır açılıyor
		Row totalRow = sheet.createRow(sheet.getLastRowNum() + 1);

	    // "TOTAL STOCK" yazısı ve toplam değeri ekleniyor
		totalRow.createCell(3).setCellValue("TOTAL STOCK:");
		totalRow.createCell(4).setCellValue(totalStock);
	}

	private void autoSizeColumns(Sheet sheet, int numColumns) {
	    // Tüm sütunların genişliğini otomatik ayarlıyoruz
	    for (int i = 0; i < numColumns; i++) {
	        sheet.autoSizeColumn(i);
		}

	}

}
