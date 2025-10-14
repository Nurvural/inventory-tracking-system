package com.example.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Firm;

@Repository
public interface FirmRepository extends JpaRepository<Firm, Long>{

    List<Firm> findAllByDeletedFalse(Sort sort);
    Optional<Firm> findByIdAndDeletedFalse(Long id);
	List<Firm> findByDeletedFalseAndNameContainingIgnoreCaseOrTaxNumberContainingIgnoreCase(String keyword,
			String keyword2);
	boolean existsByNameAndDeletedFalse(String name);
	boolean existsByNameAndDeletedFalseAndIdNot(String name, Long id);

	boolean existsByTaxNumberAndDeletedFalse(String taxNumber);
	boolean existsByTaxNumberAndDeletedFalseAndIdNot(String taxNumber, Long id);

}
