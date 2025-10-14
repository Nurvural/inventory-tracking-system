package com.example.inventory.dto.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockReport {
	private Long id;
    private String name;
    private String barcode;
    private String categoryName;
    private Integer stockQuantity;
    private String stockStatus; // LOW, MEDIUM, HIGH
    private BigDecimal unitPrice;
}