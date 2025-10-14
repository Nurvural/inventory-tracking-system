package com.example.inventory.mapper;

import com.example.inventory.dto.payment.PaymentCreateDTO;
import com.example.inventory.dto.payment.PaymentResponseDTO;
import com.example.inventory.entity.Payment;
import com.example.inventory.entity.Sale;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-14T15:08:15+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Payment.PaymentBuilder payment = Payment.builder();

        payment.note( dto.getNote() );
        payment.paidAmount( dto.getPaidAmount() );
        payment.paymentMethod( dto.getPaymentMethod() );

        return payment.build();
    }

    @Override
    public PaymentResponseDTO toResponseDTO(Payment payment, Sale sale, BigDecimal totalPaid) {
        if ( payment == null && sale == null && totalPaid == null ) {
            return null;
        }

        PaymentResponseDTO.PaymentResponseDTOBuilder paymentResponseDTO = PaymentResponseDTO.builder();

        if ( payment != null ) {
            paymentResponseDTO.note( payment.getNote() );
            paymentResponseDTO.paidAmount( payment.getPaidAmount() );
            paymentResponseDTO.paymentMethod( payment.getPaymentMethod() );
        }
        if ( sale != null ) {
            paymentResponseDTO.totalAmount( sale.getTotalAmount() );
        }
        paymentResponseDTO.saleId( payment.getSale().getId() );
        paymentResponseDTO.firmId( payment.getFirm().getId() );
        paymentResponseDTO.remainingAmount( sale.getTotalAmount().subtract(totalPaid) );
        paymentResponseDTO.id( payment.getId() );
        paymentResponseDTO.createdAt( payment.getCreatedAt() );
        paymentResponseDTO.updatedAt( payment.getUpdatedAt() );

        return paymentResponseDTO.build();
    }
}
