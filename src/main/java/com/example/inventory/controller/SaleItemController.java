package com.example.inventory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;
import com.example.inventory.service.SaleItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/saleitems")
@RequiredArgsConstructor
public class SaleItemController {

    private final SaleItemService saleItemService;

    @PostMapping("/{saleId}")
    public ResponseEntity<SaleItemResponse> createSaleItem(
            @PathVariable Long saleId,
            @RequestBody SaleItemCreateRequest request) {
        return ResponseEntity.ok(saleItemService.createSaleItem(saleId, request));
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<List<SaleItemResponse>> getSaleItemsBySaleId(@PathVariable Long saleId) {
        return ResponseEntity.ok(saleItemService.getSaleItemsBySaleId(saleId));
    }
}
