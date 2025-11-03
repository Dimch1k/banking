package com.example.banking.mapper;

import com.example.banking.dto.TransactionDto;
import com.example.banking.entity.Transaction;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionTime());
    }
}
