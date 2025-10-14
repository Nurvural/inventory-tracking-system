package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.sale.SaleCreateRequest;
import com.example.inventory.dto.sale.SaleResponse;
import com.example.inventory.dto.sale.SaleUpdateRequest;

public interface SaleService {

    SaleResponse createSale(SaleCreateRequest request);

    SaleResponse updateSale(Long id, SaleUpdateRequest request);

    SaleResponse getSaleById(Long id);

    List<SaleResponse> getAllSales();

}