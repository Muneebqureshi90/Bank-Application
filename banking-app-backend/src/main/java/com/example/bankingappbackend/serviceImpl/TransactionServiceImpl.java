package com.example.bankingappbackend.serviceImpl;

import com.example.bankingappbackend.dto.TransactionDto;
import com.example.bankingappbackend.entity.Transaction;
import com.example.bankingappbackend.repository.TransactionRepository;
import com.example.bankingappbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved SUCCESSFULLY");
    }


}
