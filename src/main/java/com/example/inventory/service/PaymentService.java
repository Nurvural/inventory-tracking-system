package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.payment.PaymentCreateDTO;
import com.example.inventory.dto.payment.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(Long saleId, PaymentCreateDTO dto);
    List<PaymentResponseDTO> getPaymentsBySale(Long saleId);
    List<PaymentResponseDTO> getAllPayments();
}
