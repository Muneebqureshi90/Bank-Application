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
@Schema(description = "Request to transfer money between accounts")
public class Transfer {
    @Schema(description = "The source account number")
    private String sourceAccountNumber;

    @Schema(description = "The destination account number")
    private String destinationAccountNumber;

    @Schema(description = "The amount to transfer")
    private BigDecimal amount;
}
