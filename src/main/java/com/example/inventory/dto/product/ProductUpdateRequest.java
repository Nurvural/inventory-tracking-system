package com.example.inventory.dto.product;

import java.math.BigDecimal;

import com.example.inventory.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @NotNull(message = "Kategori ID boş olamaz")
    private Long categoryId;

    @NotBlank(message = "Ürün adı boş olamaz")
    @Size(max = 255, message = "Ürün adı en fazla 255 karakter olabilir")
    private String name;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;

    @NotNull(message = "Stok miktarı boş olamaz")
    private Integer stockQuantity;

    @NotNull(message = "Birim fiyat boş olamaz")
    @Positive(message = "Birim fiyat pozitif olmalı")
    private BigDecimal unitPrice;

    private Status status;
}
