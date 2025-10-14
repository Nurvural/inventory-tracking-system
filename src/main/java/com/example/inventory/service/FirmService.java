package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;

public interface FirmService {

    FirmResponse createFirm(FirmCreateRequest request);

    FirmResponse updateFirm(Long id, FirmUpdateRequest request);

    List<FirmResponse> searchFirms(String keyword);

    void deleteFirm(Long id);

    FirmResponse getFirmById(Long id);

    List<FirmResponse> getAllFirms();
}
