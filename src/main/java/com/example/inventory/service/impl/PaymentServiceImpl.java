package com.example.inventory.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.payment.PaymentCreateDTO;
import com.example.inventory.dto.payment.PaymentResponseDTO;
import com.example.inventory.entity.Payment;
import com.example.inventory.entity.Sale;
import com.example.inventory.mapper.PaymentMapper;
import com.example.inventory.repository.PaymentRepository;
import com.example.inventory.repository.SaleRepository;
import com.example.inventory.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDTO createPayment(Long saleId, PaymentCreateDTO dto) {
        Sale sale = getSaleOrThrow(saleId);
        Payment payment = buildPayment(sale, dto);
        Payment savedPayment = paymentRepository.save(payment);

       // updateSaleStatus(sale);
        BigDecimal totalPaid = calculateTotalPaid(sale);
        return paymentMapper.toResponseDTO(savedPayment, sale, totalPaid);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsBySale(Long saleId) {
        List<Payment> payments = paymentRepository.findBySaleId(saleId);
        if (payments.isEmpty()) {
            throw new IllegalArgumentException("Sale ID " + saleId + " için ödeme bulunamadı");
        }
        return paymentRepository.findBySaleId(saleId).stream()
                .map(payment -> {
                    Sale sale = payment.getSale();
                    BigDecimal totalPaid = calculateTotalPaid(sale);
                    return paymentMapper.toResponseDTO(payment, sale, totalPaid);
                })
                .toList();
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> {
                    Sale sale = payment.getSale();
                    BigDecimal totalPaid = calculateTotalPaid(sale);
                    return paymentMapper.toResponseDTO(payment, sale, totalPaid);
                })
                .toList();
    }

    private Sale getSaleOrThrow(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with id: " + saleId));
    }

    private Payment buildPayment(Sale sale, PaymentCreateDTO dto) {
        return Payment.builder()
                .sale(sale)
                .firm(sale.getFirm())
                .amount(sale.getTotalAmount())
                .paidAmount(dto.getPaidAmount())
                .paymentMethod(dto.getPaymentMethod())
                .note(dto.getNote())
                .build();
    }

   /* private void updateSaleStatus(Sale sale) {
        BigDecimal totalPaid = calculateTotalPaid(sale);
        BigDecimal remaining = sale.getTotalAmount().subtract(totalPaid);

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Ödeme toplam satış tutarını aşamaz. Kalan: " + remaining);
        }

        sale.setStatus(determineSaleStatus(sale.getTotalAmount(), totalPaid));
        saleRepository.save(sale);
    }*/

    private BigDecimal calculateTotalPaid(Sale sale) {
        BigDecimal totalPaid = paymentRepository.sumPaidAmountBySaleId(sale.getId());
        return totalPaid != null ? totalPaid : BigDecimal.ZERO;
    }

    /*private SaleStatus determineSaleStatus(BigDecimal totalAmount, BigDecimal totalPaid) {
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            return SaleStatus.PENDING;
        } else if (totalPaid.compareTo(totalAmount) < 0) {
            return SaleStatus.PARTIAL_PAID;
        } else {
            return SaleStatus.PAID;
        }
    }*/
}
