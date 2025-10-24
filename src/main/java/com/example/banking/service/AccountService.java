package com.example.banking.service;

import com.example.banking.dto.AccountDto;
import com.example.banking.entity.Account;
import com.example.banking.exception.AccountException;
import com.example.banking.mapper.AccountMapper;
import com.example.banking.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.toAccount(accountDto);
        account.setBalance(0.0);
        account = accountRepository.save(account);
        return AccountMapper.toDto(account);
    }

    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        return AccountMapper.toDto(account);
    }

    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::toDto)
                .toList();
    }

    public void deleteAccount(Long id) {
        accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        account = accountRepository.save(account);
        return AccountMapper.toDto(account);
    }

    @Transactional
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        account = accountRepository.save(account);
        return AccountMapper.toDto(account);
    }
}
