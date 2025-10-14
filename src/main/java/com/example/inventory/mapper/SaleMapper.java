package com.example.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.inventory.dto.sale.SaleCreateRequest;
import com.example.inventory.dto.sale.SaleResponse;
import com.example.inventory.dto.sale.SaleUpdateRequest;
import com.example.inventory.entity.Sale;

@Mapper(componentModel = "spring", uses = {SaleItemMapper.class})
public interface SaleMapper {

    @Mapping(source = "firmId", target = "firm.id")
    @Mapping(target = "status", expression = "java(com.example.inventory.enums.SaleStatus.PENDING)")
    Sale toEntity(SaleCreateRequest request);

    @Mapping(source = "firm.id", target = "firmId")
    @Mapping(source = "user.id", target = "userId")
    SaleResponse toResponse(Sale sale);

    void updateEntityFromDto(SaleUpdateRequest request, @MappingTarget Sale sale);
}
