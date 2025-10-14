package com.example.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.dto.payment.PaymentCreateDTO;
import com.example.inventory.dto.payment.PaymentResponseDTO;
import com.example.inventory.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Sale için ödeme oluşturma
    @PostMapping("/sale/{saleId}")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @PathVariable Long saleId,
            @RequestBody @Valid PaymentCreateDTO dto) {

        PaymentResponseDTO response = paymentService.createPayment(saleId, dto);
        return ResponseEntity.ok(response);
    }
    // Sale'e ait tüm ödemeleri listeleme
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<?> getPaymentsBySale(@PathVariable Long saleId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySale(saleId));
    }

    // Tüm ödemeleri listeleme
    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
