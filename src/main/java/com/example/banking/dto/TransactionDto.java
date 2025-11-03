package com.example.banking.dto;

import com.example.banking.config.TransactionType;

import java.time.LocalDateTime;

public record TransactionDto(Long id, Long accountId, double amount, TransactionType transactionType, LocalDateTime transactionTime) {
}
