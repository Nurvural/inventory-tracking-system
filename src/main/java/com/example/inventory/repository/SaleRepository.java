package com.example.inventory.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.firm.id = :firmId")
    Optional<BigDecimal> sumTotalAmountByFirmId(@Param("firmId") Long firmId);
}
