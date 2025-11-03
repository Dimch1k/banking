package com.example.banking.dto;

public record TransferFoundDto(Long fromAccountId, Long toAccountId, double amount) {
}
