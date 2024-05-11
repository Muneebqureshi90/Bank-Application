package com.example.bankingappbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Information about an account")
public class AccounInfo {
    @Schema(description = "The name of the account holder")
    private String accountName;

    @Schema(description = "The account number")
    private String accountNumber;

    @Schema(description = "The balance of the account")
    private BigDecimal accountBalance;
}
