package com.example.inventory.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.inventory.enums.StockLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String categoryName;
    private String name;
    private String barcode;
    private String description;
    private Integer stockQuantity;
    private BigDecimal unitPrice;
    private String status;
    private StockLevel stockStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
