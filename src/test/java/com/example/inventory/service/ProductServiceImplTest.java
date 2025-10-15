package com.example.inventory.service;

import com.example.inventory.dto.product.ProductCreateRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.entity.Category;
import com.example.inventory.entity.Product;
import com.example.inventory.enums.Status;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- createProduct() testi ---
    @Test
    void shouldCreateProductSuccessfully() {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Laptop")
                .barcode("ABC123")
                .stockQuantity(10)
                .unitPrice(new BigDecimal("10000"))
                .categoryId(1L)
                .build();

        Category category = new Category();
        category.setId(1L);
        category.setName("Elektronik");

        Product entity = Product.builder()
                .id(1L)
                .name("Laptop")
                .barcode("ABC123")
                .stockQuantity(10)
                .unitPrice(new BigDecimal("10000"))
                .status(Status.ACTIVE)
                .category(category)
                .deleted(false)
                .build();

        ProductResponse response = new ProductResponse();
        response.setName("Laptop");

        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsByBarcodeAndDeletedFalse("ABC123")).thenReturn(false);
        when(productMapper.toEntity(request)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(productMapper.toResponse(entity)).thenReturn(response);

        ProductResponse result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository).save(entity);
    }

    // --- getProductById() testi ---
    @Test
    void shouldReturnProductWhenFound() {
        Product product = Product.builder()
                .id(1L)
                .name("Mouse")
                .status(Status.ACTIVE)
                .deleted(false)
                .build();

        ProductResponse response = new ProductResponse();
        response.setName("Mouse");

        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.getProductById(1L);

        assertEquals("Mouse", result.getName());
    }

    // --- deleteProduct() testi ---
    @Test
    void shouldSoftDeleteProduct() {
        Product product = Product.builder()
                .id(1L)
                .deleted(false)
                .build();

        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        assertTrue(product.isDeleted());
        verify(productRepository).save(product);
    }

    // --- getProductById() hata testi ---
    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }
}
