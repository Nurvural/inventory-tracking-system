package com.example.inventory.dto.firm;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirmResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String contactPerson;
    private String taxNumber;
    private String taxOffice;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}