package com.example.bankingappbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor // Add all-args constructor for @Builder
@Builder
@Schema(description = "Transaction Entity")
@JsonIgnoreProperties({"hibernateLazyInitializer", "id"})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
    @Column(name = "created_at") // Changed column name to follow Java naming conventions
    @CreationTimestamp
    private LocalDateTime createdAt; // Changed variable name to follow Java naming conventions

    @Column(name = "modified_at") // Changed column name to follow Java naming conventions
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
