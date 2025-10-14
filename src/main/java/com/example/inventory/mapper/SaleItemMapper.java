package com.example.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Sale;
import com.example.inventory.entity.SaleItem;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SaleItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", source = "sale")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "unitPrice", expression = "java(product.getUnitPrice())")
    @Mapping(target = "lineTotal", expression = "java(product.getUnitPrice().multiply(java.math.BigDecimal.valueOf(dto.getQuantity())))")
    SaleItem toEntity(SaleItemCreateRequest dto, Sale sale, Product product);

    @Mapping(target = "saleId", source = "sale.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "barcode", source = "product.barcode")
    @Mapping(target = "categoryName", expression = "java(entity.getProduct().getCategory() != null ? entity.getProduct().getCategory().getName() : null)")
    SaleItemResponse toResponse(SaleItem entity);

}