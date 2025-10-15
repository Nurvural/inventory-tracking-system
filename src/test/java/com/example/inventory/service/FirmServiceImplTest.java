package com.example.inventory.service;

import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;
import com.example.inventory.entity.Firm;
import com.example.inventory.enums.Status;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.mapper.FirmMapper;
import com.example.inventory.repository.FirmRepository;
import com.example.inventory.service.impl.FirmServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirmServiceImplTest {

    @Mock
    private FirmRepository firmRepository;

    @Mock
    private FirmMapper firmMapper;

    @InjectMocks
    private FirmServiceImpl firmService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- createFirm() testi ---
    @Test
    void shouldCreateFirmSuccessfully() {
        FirmCreateRequest request = new FirmCreateRequest();
        request.setName("ABC Ltd");
        request.setTaxNumber("1234567890");

        Firm firmEntity = Firm.builder()
                .id(1L)
                .name("ABC Ltd")
                .taxNumber("1234567890")
                .balance(BigDecimal.ZERO)
                .build();

        FirmResponse response = new FirmResponse();
        response.setName("ABC Ltd");

        when(firmRepository.existsByNameAndDeletedFalse("ABC Ltd")).thenReturn(false);
        when(firmRepository.existsByTaxNumberAndDeletedFalse("1234567890")).thenReturn(false);
        when(firmMapper.toEntity(request)).thenReturn(firmEntity);
        when(firmRepository.save(firmEntity)).thenReturn(firmEntity);
        when(firmMapper.toResponse(firmEntity)).thenReturn(response);

        FirmResponse result = firmService.createFirm(request);

        assertNotNull(result);
        assertEquals("ABC Ltd", result.getName());
        verify(firmRepository).save(firmEntity);
    }

    // --- updateFirm() testi ---
    @Test
    void shouldUpdateExistingFirm() {
        FirmUpdateRequest request = new FirmUpdateRequest();
        request.setName("Updated Firm");

        Firm existingFirm = Firm.builder()
                .id(1L)
                .name("Old Firm")
                .taxNumber("999")
                .status(Status.ACTIVE)
                .build();

        when(firmRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(existingFirm));
        when(firmRepository.save(existingFirm)).thenReturn(existingFirm);

        FirmResponse response = new FirmResponse();
        response.setName("Updated Firm");
        when(firmMapper.toResponse(existingFirm)).thenReturn(response);

        FirmResponse result = firmService.updateFirm(1L, request);

        assertEquals("Updated Firm", result.getName());
        verify(firmRepository).save(existingFirm);
    }

    // --- getFirmById() testi ---
    @Test
    void shouldReturnFirmWhenFound() {
        Firm firm = Firm.builder()
                .id(1L)
                .name("XYZ Ltd")
                .status(Status.ACTIVE)
                .deleted(false)
                .build();

        when(firmRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firm));

        FirmResponse response = new FirmResponse();
        response.setName("XYZ Ltd");
        when(firmMapper.toResponse(firm)).thenReturn(response);

        FirmResponse result = firmService.getFirmById(1L);

        assertEquals("XYZ Ltd", result.getName());
    }

    // --- deleteFirm() testi ---
    @Test
    void shouldSoftDeleteFirm() {
        Firm firm = Firm.builder()
                .id(1L)
                .deleted(false)
                .status(Status.ACTIVE)
                .build();

        when(firmRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firm));

        firmService.deleteFirm(1L);

        assertTrue(firm.isDeleted());
        verify(firmRepository).save(firm);
    }

    // --- getFirmById() hata testi ---
    @Test
    void shouldThrowWhenFirmNotFound() {
        when(firmRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> firmService.getFirmById(99L));
    }
}
