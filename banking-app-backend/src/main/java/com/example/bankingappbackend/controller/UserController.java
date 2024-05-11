package com.example.bankingappbackend.controller;

import com.example.bankingappbackend.dto.*;
import com.example.bankingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "scheme1")
@RestController
@RequestMapping("/api/user/")
@Tag(name = "User Controller", description = "This is User Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    @Operation(summary = "Create a new user", description = "Creates a new user account")
    public BankResponce createUser(@RequestBody UserDto userDto) {
        return userService.createBank(userDto);
    }

    @GetMapping("/balanceEnquiry")
    @Operation(summary = "Balance Enquiry", description = "Get the account balance")
    public BankResponce balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balance(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    @Operation(summary = "Name Enquiry", description = "Get the account holder's name")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }

    @PostMapping("/credit")
    @Operation(summary = "Credit Account", description = "Credit the specified account")
    public BankResponce creditAccount(@RequestBody @Schema(description = "Credit Debit Object") CreditDebit creditDebit) {
        return userService.creditAccount(creditDebit);
    }

    @PostMapping("/debit")
    @Operation(summary = "Debit Account", description = "Debit the specified account")
    public BankResponce debitAccount(@RequestBody @Schema(description = "Credit Debit Object") CreditDebit creditDebit) {
        return userService.debitAccount(creditDebit);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer Amount", description = "Transfer amount from one account to another")
    public BankResponce transfer(@RequestBody @Schema(description = "Transfer Object") Transfer transfer) {
        return userService.transfer(transfer);
    }
}
