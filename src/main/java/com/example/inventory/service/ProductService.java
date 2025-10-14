package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.product.ProductCreateRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.dto.product.ProductStockReport;
import com.example.inventory.dto.product.ProductUpdateRequest;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> searchProducts(String keyword);

    List<ProductStockReport> getStockReport();

    byte[] generateStockReportExcel();
}