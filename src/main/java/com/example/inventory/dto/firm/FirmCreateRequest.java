package com.example.inventory.dto.firm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirmCreateRequest {

    @NotBlank(message = "Firm name is required")
    @Size(max = 100, message = "Firm name must be less than 100 characters")
    private String name;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Size(max = 100, message = "Contact person must be less than 100 characters")
    private String contactPerson;

    @Size(max = 50, message = "Tax number must be less than 50 characters")
    private String taxNumber;

    @Size(max = 100, message = "Tax office must be less than 100 characters")
    private String taxOffice;
}
