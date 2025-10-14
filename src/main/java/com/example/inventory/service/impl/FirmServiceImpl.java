package com.example.inventory.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;
import com.example.inventory.entity.Firm;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.mapper.FirmMapper;
import com.example.inventory.repository.FirmRepository;
import com.example.inventory.service.FirmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FirmServiceImpl implements FirmService {

    private final FirmRepository firmRepository;
    private final FirmMapper firmMapper;

    @Override
    public FirmResponse createFirm(FirmCreateRequest request) {
        validateUniqueName(request.getName(), null);
        validateUniqueTaxNumber(request.getTaxNumber(), null);
        Firm firm = firmMapper.toEntity(request);
       // firm.setDeleted(false); // yeni firma aktif
        return firmMapper.toResponse(firmRepository.save(firm));
    }

    @Override
    public FirmResponse updateFirm(Long id, FirmUpdateRequest request) {
        Firm existing = firmRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Firm not found with id: " + id));
        firmMapper.updateEntityFromDto(request, existing);
        return firmMapper.toResponse(firmRepository.save(existing));
    }

    @Override
    public void deleteFirm(Long id) {
        Firm firm = firmRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Firm not found with id: " + id));
        firm.setDeleted(true); // soft delete
        firmRepository.save(firm);
    }

    @Override
    public FirmResponse getFirmById(Long id) {
        Firm firm = firmRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Firm not found with id: " + id));
        return firmMapper.toResponse(firm);
    }

    @Override
    public List<FirmResponse> getAllFirms() {
        return firmRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(firmMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FirmResponse> searchFirms(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Arama terimi boş olamaz.");
        }

        List<Firm> firms = firmRepository
                .findByDeletedFalseAndNameContainingIgnoreCaseOrTaxNumberContainingIgnoreCase(keyword, keyword);

        if (firms.isEmpty()) {
            throw new ResourceNotFoundException("Arama kriterine uygun firma bulunamadı: " + keyword);
        }

        return firms.stream()
                .map(firmMapper::toResponse)
                .collect(Collectors.toList());
    }
 // ---------------- Private validation methods ----------------
    private void validateUniqueName(String name, Long existingId) {
        boolean exists = existingId == null 
            ? firmRepository.existsByNameAndDeletedFalse(name)
            : firmRepository.existsByNameAndDeletedFalseAndIdNot(name, existingId);
        if (exists) {
            throw new IllegalArgumentException("Firma adı zaten mevcut: " + name);
        }
    }

    private void validateUniqueTaxNumber(String taxNumber, Long existingId) {
        if (taxNumber == null || taxNumber.isBlank()) return;

        boolean exists = existingId == null
            ? firmRepository.existsByTaxNumberAndDeletedFalse(taxNumber)
            : firmRepository.existsByTaxNumberAndDeletedFalseAndIdNot(taxNumber, existingId);
        if (exists) {
            throw new IllegalArgumentException("Vergi numarası zaten mevcut: " + taxNumber);
        }
    }
}
