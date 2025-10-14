package com.example.inventory.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.dto.firm.FirmBalanceResponse;
import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;
import com.example.inventory.service.FirmReportService;
import com.example.inventory.service.FirmService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/firms")
@RequiredArgsConstructor
public class FirmController {

    private final FirmService firmService;
    private final FirmReportService firmReportService;

    @PostMapping
    public ResponseEntity<FirmResponse> create(@Valid @RequestBody FirmCreateRequest request) {
        FirmResponse response = firmService.createFirm(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FirmResponse> update(@PathVariable Long id, @Valid @RequestBody FirmUpdateRequest request) {
        FirmResponse response = firmService.updateFirm(id, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<List<FirmResponse>> searchFirms(@RequestParam String keyword) {
        return ResponseEntity.ok(firmService.searchFirms(keyword));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        firmService.deleteFirm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FirmResponse> getById(@PathVariable Long id) {
        FirmResponse response = firmService.getFirmById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FirmResponse>> getAll() {
        List<FirmResponse> responses = firmService.getAllFirms();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/balances")
    public List<FirmBalanceResponse> getFirmBalances() {
        return firmReportService.getAllFirmBalances();
    }
    @GetMapping("/balances/export")
    public ResponseEntity<byte[]> exportFirmBalances() {
        byte[] excelData = firmReportService.exportFirmBalancesToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=firm_balances.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

}
