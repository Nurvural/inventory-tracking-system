package com.example.inventory.dto.sale;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleCreateRequest {
    private Long firmId;
    private List<SaleItemCreateRequest> items;

}