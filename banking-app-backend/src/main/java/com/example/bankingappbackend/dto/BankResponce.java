package com.example.bankingappbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response from the bank operations")
public class BankResponce {
    @Schema(description = "Response code from the bank operation")
    private String responceCode;

    @Schema(description = "Response message from the bank operation")
    private String responceMessage;

    @Schema(description = "Information about the account")
    private AccounInfo accounInfo;
}
