package com.example.inventory.mapper;

import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Sale;
import com.example.inventory.entity.SaleItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-14T15:08:16+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SaleItemMapperImpl implements SaleItemMapper {

    @Override
    public SaleItem toEntity(SaleItemCreateRequest dto, Sale sale, Product product) {
        if ( dto == null && sale == null && product == null ) {
            return null;
        }

        SaleItem.SaleItemBuilder saleItem = SaleItem.builder();

        if ( dto != null ) {
            if ( dto.getQuantity() != null ) {
                saleItem.quantity( dto.getQuantity() );
            }
        }
        if ( sale != null ) {
            saleItem.sale( sale );
        }
        if ( product != null ) {
            saleItem.product( product );
        }
        saleItem.unitPrice( product.getUnitPrice() );
        saleItem.lineTotal( product.getUnitPrice().multiply(java.math.BigDecimal.valueOf(dto.getQuantity())) );

        return saleItem.build();
    }

    @Override
    public SaleItemResponse toResponse(SaleItem entity) {
        if ( entity == null ) {
            return null;
        }

        SaleItemResponse.SaleItemResponseBuilder saleItemResponse = SaleItemResponse.builder();

        Long id = entitySaleId( entity );
        if ( id != null ) {
            saleItemResponse.saleId( id );
        }
        Long id1 = entityProductId( entity );
        if ( id1 != null ) {
            saleItemResponse.productId( id1 );
        }
        String name = entityProductName( entity );
        if ( name != null ) {
            saleItemResponse.productName( name );
        }
        String barcode = entityProductBarcode( entity );
        if ( barcode != null ) {
            saleItemResponse.barcode( barcode );
        }
        if ( entity.getId() != null ) {
            saleItemResponse.id( entity.getId() );
        }
        if ( entity.getLineTotal() != null ) {
            saleItemResponse.lineTotal( entity.getLineTotal() );
        }
        if ( entity.getQuantity() != null ) {
            saleItemResponse.quantity( entity.getQuantity() );
        }
        if ( entity.getUnitPrice() != null ) {
            saleItemResponse.unitPrice( entity.getUnitPrice() );
        }

        saleItemResponse.categoryName( entity.getProduct().getCategory() != null ? entity.getProduct().getCategory().getName() : null );

        return saleItemResponse.build();
    }

    private Long entitySaleId(SaleItem saleItem) {
        if ( saleItem == null ) {
            return null;
        }
        Sale sale = saleItem.getSale();
        if ( sale == null ) {
            return null;
        }
        Long id = sale.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProductId(SaleItem saleItem) {
        if ( saleItem == null ) {
            return null;
        }
        Product product = saleItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProductName(SaleItem saleItem) {
        if ( saleItem == null ) {
            return null;
        }
        Product product = saleItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityProductBarcode(SaleItem saleItem) {
        if ( saleItem == null ) {
            return null;
        }
        Product product = saleItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String barcode = product.getBarcode();
        if ( barcode == null ) {
            return null;
        }
        return barcode;
    }
}
