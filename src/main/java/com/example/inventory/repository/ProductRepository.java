package com.example.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByDeletedFalse(Sort sort);

	Optional<Product> findByIdAndDeletedFalse(Long id);

	boolean existsByBarcodeAndDeletedFalse(String barcode);

    Optional<Product> findByBarcodeAndDeletedFalse(String barcode);

    List<Product> findByDeletedFalseAndNameContainingIgnoreCaseOrBarcodeContainingIgnoreCase(String name, String barcode);

    Optional<Product> findByNameAndDeletedFalse(String name);
}