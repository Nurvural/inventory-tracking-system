package com.example.inventory.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.inventory.dto.payment.PaymentCreateDTO;
import com.example.inventory.dto.payment.PaymentResponseDTO;
import com.example.inventory.entity.Payment;
import com.example.inventory.entity.Sale;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(PaymentCreateDTO dto);

    @Mapping(target = "saleId", expression = "java(payment.getSale().getId())")
    @Mapping(target = "firmId", expression = "java(payment.getFirm().getId())")
    @Mapping(target = "remainingAmount", expression = "java(sale.getTotalAmount().subtract(totalPaid))")
  //  @Mapping(target = "saleStatus", source = "sale.status")
    @Mapping(target = "id", expression = "java(payment.getId())")
    @Mapping(target = "createdAt", expression = "java(payment.getCreatedAt())")
    @Mapping(target = "updatedAt", expression = "java(payment.getUpdatedAt())")
    PaymentResponseDTO toResponseDTO(Payment payment, Sale sale, BigDecimal totalPaid);
}