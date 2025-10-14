package com.example.inventory.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	List<Payment> findBySaleId(Long saleId);

	@Query("SELECT SUM(p.paidAmount) FROM Payment p WHERE p.sale.id = :saleId")
	BigDecimal sumPaidAmountBySaleId(@Param("saleId") Long saleId);

	@Query("SELECT SUM(p.paidAmount) FROM Payment p WHERE p.sale.firm.id = :firmId")
	Optional<BigDecimal> sumPaidAmountByFirmId(@Param("firmId") Long firmId);
}
