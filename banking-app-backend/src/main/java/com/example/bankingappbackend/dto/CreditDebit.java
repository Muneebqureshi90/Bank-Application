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
@Schema(description = "Request to credit or debit an account")
public class CreditDebit {
    @Schema(description = "The account number")
    private String accountNumber;

    @Schema(description = "The amount to credit or debit")
    private BigDecimal amount;
}
