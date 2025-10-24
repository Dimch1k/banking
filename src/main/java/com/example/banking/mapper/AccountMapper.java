package com.example.banking.mapper;

import com.example.banking.dto.AccountDto;
import com.example.banking.entity.Account;

public class AccountMapper {

    public static AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getAccountHolderName(), account.getBalance());
    }

    public static Account toAccount(AccountDto dto) {
        return new Account(dto.id(), dto.accountHolderName(), dto.balance());
    }
}
