package com.example.inventory.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemCreateRequest {
	private Long productId; // Öncelikli olarak ID ile seçim
	private String barcode; // Eğer ID yoksa barkod ile seçim
	private String name;
	private Integer quantity;

}
