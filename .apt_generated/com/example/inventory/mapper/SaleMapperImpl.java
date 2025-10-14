package com.example.inventory.mapper;

import com.example.inventory.dto.sale.SaleCreateRequest;
import com.example.inventory.dto.sale.SaleItemCreateRequest;
import com.example.inventory.dto.sale.SaleItemResponse;
import com.example.inventory.dto.sale.SaleResponse;
import com.example.inventory.dto.sale.SaleUpdateRequest;
import com.example.inventory.entity.Firm;
import com.example.inventory.entity.Sale;
import com.example.inventory.entity.SaleItem;
import com.example.inventory.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-14T15:08:16+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SaleMapperImpl implements SaleMapper {

    @Autowired
    private SaleItemMapper saleItemMapper;

    @Override
    public Sale toEntity(SaleCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Sale.SaleBuilder sale = Sale.builder();

        sale.firm( saleCreateRequestToFirm( request ) );
        sale.items( saleItemCreateRequestListToSaleItemList( request.getItems() ) );

        sale.status( com.example.inventory.enums.SaleStatus.PENDING );

        return sale.build();
    }

    @Override
    public SaleResponse toResponse(Sale sale) {
        if ( sale == null ) {
            return null;
        }

        SaleResponse.SaleResponseBuilder saleResponse = SaleResponse.builder();

        saleResponse.firmId( saleFirmId( sale ) );
        saleResponse.userId( saleUserId( sale ) );
        saleResponse.createdAt( sale.getCreatedAt() );
        saleResponse.id( sale.getId() );
        saleResponse.items( saleItemListToSaleItemResponseList( sale.getItems() ) );
        saleResponse.status( sale.getStatus() );
        saleResponse.totalAmount( sale.getTotalAmount() );
        saleResponse.updatedAt( sale.getUpdatedAt() );

        return saleResponse.build();
    }

    @Override
    public void updateEntityFromDto(SaleUpdateRequest request, Sale sale) {
        if ( request == null ) {
            return;
        }

        if ( sale.getItems() != null ) {
            List<SaleItem> list = saleItemCreateRequestListToSaleItemList( request.getItems() );
            if ( list != null ) {
                sale.getItems().clear();
                sale.getItems().addAll( list );
            }
            else {
                sale.setItems( null );
            }
        }
        else {
            List<SaleItem> list = saleItemCreateRequestListToSaleItemList( request.getItems() );
            if ( list != null ) {
                sale.setItems( list );
            }
        }
    }

    protected Firm saleCreateRequestToFirm(SaleCreateRequest saleCreateRequest) {
        if ( saleCreateRequest == null ) {
            return null;
        }

        Firm.FirmBuilder firm = Firm.builder();

        firm.id( saleCreateRequest.getFirmId() );

        return firm.build();
    }

    protected SaleItem saleItemCreateRequestToSaleItem(SaleItemCreateRequest saleItemCreateRequest) {
        if ( saleItemCreateRequest == null ) {
            return null;
        }

        SaleItem.SaleItemBuilder saleItem = SaleItem.builder();

        saleItem.quantity( saleItemCreateRequest.getQuantity() );

        return saleItem.build();
    }

    protected List<SaleItem> saleItemCreateRequestListToSaleItemList(List<SaleItemCreateRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<SaleItem> list1 = new ArrayList<SaleItem>( list.size() );
        for ( SaleItemCreateRequest saleItemCreateRequest : list ) {
            list1.add( saleItemCreateRequestToSaleItem( saleItemCreateRequest ) );
        }

        return list1;
    }

    private Long saleFirmId(Sale sale) {
        if ( sale == null ) {
            return null;
        }
        Firm firm = sale.getFirm();
        if ( firm == null ) {
            return null;
        }
        Long id = firm.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long saleUserId(Sale sale) {
        if ( sale == null ) {
            return null;
        }
        User user = sale.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<SaleItemResponse> saleItemListToSaleItemResponseList(List<SaleItem> list) {
        if ( list == null ) {
            return null;
        }

        List<SaleItemResponse> list1 = new ArrayList<SaleItemResponse>( list.size() );
        for ( SaleItem saleItem : list ) {
            list1.add( saleItemMapper.toResponse( saleItem ) );
        }

        return list1;
    }
}
