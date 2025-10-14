package com.example.inventory.mapper;

import com.example.inventory.dto.firm.FirmCreateRequest;
import com.example.inventory.dto.firm.FirmResponse;
import com.example.inventory.dto.firm.FirmUpdateRequest;
import com.example.inventory.entity.Firm;
import com.example.inventory.enums.Status;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-14T15:53:21+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class FirmMapperImpl implements FirmMapper {

    @Override
    public Firm toEntity(FirmCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Firm.FirmBuilder firm = Firm.builder();

        firm.address( request.getAddress() );
        firm.contactPerson( request.getContactPerson() );
        firm.email( request.getEmail() );
        firm.name( request.getName() );
        firm.phone( request.getPhone() );
        firm.taxNumber( request.getTaxNumber() );
        firm.taxOffice( request.getTaxOffice() );

        return firm.build();
    }

    @Override
    public void updateEntityFromDto(FirmUpdateRequest request, Firm firm) {
        if ( request == null ) {
            return;
        }

        firm.setAddress( request.getAddress() );
        firm.setContactPerson( request.getContactPerson() );
        firm.setEmail( request.getEmail() );
        firm.setName( request.getName() );
        firm.setPhone( request.getPhone() );
        if ( request.getStatus() != null ) {
            firm.setStatus( Enum.valueOf( Status.class, request.getStatus() ) );
        }
        else {
            firm.setStatus( null );
        }
        firm.setTaxNumber( request.getTaxNumber() );
        firm.setTaxOffice( request.getTaxOffice() );
    }

    @Override
    public FirmResponse toResponse(Firm firm) {
        if ( firm == null ) {
            return null;
        }

        FirmResponse.FirmResponseBuilder firmResponse = FirmResponse.builder();

        firmResponse.address( firm.getAddress() );
        firmResponse.balance( firm.getBalance() );
        firmResponse.contactPerson( firm.getContactPerson() );
        firmResponse.createdAt( firm.getCreatedAt() );
        firmResponse.email( firm.getEmail() );
        firmResponse.id( firm.getId() );
        firmResponse.name( firm.getName() );
        firmResponse.phone( firm.getPhone() );
        if ( firm.getStatus() != null ) {
            firmResponse.status( firm.getStatus().name() );
        }
        firmResponse.taxNumber( firm.getTaxNumber() );
        firmResponse.taxOffice( firm.getTaxOffice() );
        firmResponse.updatedAt( firm.getUpdatedAt() );

        return firmResponse.build();
    }
}
