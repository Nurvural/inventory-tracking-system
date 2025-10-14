package com.example.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;
import com.example.inventory.entity.Firm;

@Mapper(componentModel = "spring")
public interface FirmMapper {

    Firm toEntity(FirmCreateRequest request);

    void updateEntityFromDto(FirmUpdateRequest request, @MappingTarget Firm firm);

    FirmResponse toResponse(Firm firm);
}
