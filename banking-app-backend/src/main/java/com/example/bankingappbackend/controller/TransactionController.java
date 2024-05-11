package com.example.bankingappbackend.controller;

import com.example.bankingappbackend.entity.Transaction;
import com.example.bankingappbackend.serviceImpl.BankStatement;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bankStatement/")
@Tag(name = "BankStatement Controller", description = "This is BankStatement Controller")
public class TransactionController {

    private final BankStatement bankStatement;

    @GetMapping // Remove the slash from the annotation and map it to the base path
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) throws DocumentException, FileNotFoundException {
        List<Transaction> transactions = bankStatement.generateStatement(accountNumber, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }


}
