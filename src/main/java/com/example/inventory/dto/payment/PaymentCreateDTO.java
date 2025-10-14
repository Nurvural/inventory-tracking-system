package com.example.inventory.dto.payment;

import java.math.BigDecimal;

import com.example.inventory.enums.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateDTO {

    @NotNull(message = "Payment method gerekli")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Ödenen miktar gerekli")
    @DecimalMin(value = "0.01", message = "Ödenen miktar 0 olamaz")
    private BigDecimal paidAmount;

    private String note; // opsiyonel
}
