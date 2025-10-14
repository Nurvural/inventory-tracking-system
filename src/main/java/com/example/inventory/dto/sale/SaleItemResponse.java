package com.example.inventory.dto.sale;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemResponse {
    private Long id;
    private Long saleId;
    private Long productId;     // product.id ile eşleşecek
    private String productName; // product.name ile eşleşecek
    private String categoryName;
    private String barcode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
