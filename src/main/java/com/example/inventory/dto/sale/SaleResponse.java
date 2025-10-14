package com.example.inventory.dto.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.inventory.enums.SaleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {

	private Long id;
	private Long firmId;
	private Long userId;
	private BigDecimal totalAmount;
    private List<SaleItemResponse> items;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
    private SaleStatus status;

}
