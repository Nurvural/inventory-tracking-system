package com.example.inventory.mapper;

import com.example.inventory.dto.product.ProductCreateRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.dto.product.ProductStockReport;
import com.example.inventory.dto.product.ProductUpdateRequest;
import com.example.inventory.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-14T15:08:16+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.barcode( product.getBarcode() );
        productResponse.createdAt( product.getCreatedAt() );
        productResponse.description( product.getDescription() );
        productResponse.id( product.getId() );
        productResponse.name( product.getName() );
        if ( product.getStatus() != null ) {
            productResponse.status( product.getStatus().name() );
        }
        productResponse.stockQuantity( product.getStockQuantity() );
        productResponse.unitPrice( product.getUnitPrice() );
        productResponse.updatedAt( product.getUpdatedAt() );

        productResponse.categoryName( product.getCategory() != null ? product.getCategory().getName() : null );
        productResponse.stockStatus( com.example.inventory.util.StockStatusUtil.calculate(product) );

        return productResponse.build();
    }

    @Override
    public Product toEntity(ProductCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.barcode( request.getBarcode() );
        product.description( request.getDescription() );
        product.name( request.getName() );
        product.stockQuantity( request.getStockQuantity() );
        product.unitPrice( request.getUnitPrice() );

        product.status( com.example.inventory.enums.Status.ACTIVE );

        return product.build();
    }

    @Override
    public void updateEntityFromDto(ProductUpdateRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        product.setDescription( request.getDescription() );
        product.setName( request.getName() );
        product.setStockQuantity( request.getStockQuantity() );
        product.setUnitPrice( request.getUnitPrice() );

        product.setStatus( request.getStatus() != null ? request.getStatus() : com.example.inventory.enums.Status.ACTIVE );
    }

    @Override
    public ProductStockReport toStockReport(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductStockReport.ProductStockReportBuilder productStockReport = ProductStockReport.builder();

        productStockReport.barcode( product.getBarcode() );
        productStockReport.id( product.getId() );
        productStockReport.name( product.getName() );
        productStockReport.stockQuantity( product.getStockQuantity() );
        productStockReport.unitPrice( product.getUnitPrice() );

        productStockReport.categoryName( product.getCategory() != null ? product.getCategory().getName() : null );
        productStockReport.stockStatus( com.example.inventory.util.StockStatusUtil.calculate(product).name() );

        return productStockReport.build();
    }
}
