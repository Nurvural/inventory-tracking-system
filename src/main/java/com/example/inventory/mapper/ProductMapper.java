package com.example.inventory.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.inventory.dto.product.ProductCreateRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.dto.product.ProductStockReport;
import com.example.inventory.dto.product.ProductUpdateRequest;
import com.example.inventory.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Product -> ProductResponse
   // @Mapping(target = "categoryId", expression = "java(product.getCategory() != null ? product.getCategory().getId() : null)")
    @Mapping(target = "categoryName", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "stockStatus", expression = "java(com.example.inventory.util.StockStatusUtil.calculate(product))")
    //@Mapping(target = "lowStockThreshold", expression = "java(product.getLowStockThreshold() != null ? product.getLowStockThreshold() : 10)")
 //   @Mapping(target = "mediumStockThreshold", expression = "java(product.getMediumStockThreshold() != null ? product.getMediumStockThreshold() : 100)")
    ProductResponse toResponse(Product product);

    @Mapping(target = "status", expression = "java(com.example.inventory.enums.Status.ACTIVE)")
    Product toEntity(ProductCreateRequest request);

    // ProductUpdateRequest -> Product
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? request.getStatus() : com.example.inventory.enums.Status.ACTIVE)")
    void updateEntityFromDto(ProductUpdateRequest request, @MappingTarget Product product);

    @Mapping(target = "categoryName", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "stockStatus", expression = "java(com.example.inventory.util.StockStatusUtil.calculate(product).name())")
    ProductStockReport toStockReport(Product product);
}
