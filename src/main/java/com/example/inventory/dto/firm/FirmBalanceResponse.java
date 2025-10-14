package com.example.inventory.dto.firm;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirmBalanceResponse {
    private Long firmId;
    private String firmName;
    private BigDecimal totalSales;
    private BigDecimal totalPayments;
    private BigDecimal balance;
}
