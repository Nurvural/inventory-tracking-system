package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;

public interface SaleItemService {

    SaleItemResponse createSaleItem(Long saleId, SaleItemCreateRequest request);

    List<SaleItemResponse> getSaleItemsBySaleId(Long saleId);
}
