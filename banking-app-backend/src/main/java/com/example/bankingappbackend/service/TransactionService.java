package com.example.bankingappbackend.service;

import com.example.bankingappbackend.dto.TransactionDto;
import com.example.bankingappbackend.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
