package com.example.banking.service;

import com.example.banking.config.TransactionType;
import com.example.banking.dto.AccountDto;
import com.example.banking.dto.TransactionDto;
import com.example.banking.dto.TransferFoundDto;
import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import com.example.banking.exception.AccountException;
import com.example.banking.mapper.AccountMapper;
import com.example.banking.mapper.TransactionMapper;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

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

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

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

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(amount);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);


        return AccountMapper.toDto(account);
    }

    @Transactional
    public void transferFounds(TransferFoundDto transferFoundDto) {

        Account fromAccount = accountRepository.findById(transferFoundDto.fromAccountId())
                .orElseThrow(() -> new AccountException("Account not found"));
        Account toAccount = accountRepository.findById(transferFoundDto.toAccountId())
                .orElseThrow(() -> new AccountException("Account not found"));

        if (fromAccount.getBalance() < transferFoundDto.amount()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferFoundDto.amount());
        toAccount.setBalance(toAccount.getBalance() + transferFoundDto.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transactionFromAccount = new Transaction();
        transactionFromAccount.setAccountId(fromAccount.getId());
        transactionFromAccount.setAmount(transferFoundDto.amount());
        transactionFromAccount.setTransactionTime(LocalDateTime.now());
        transactionFromAccount.setTransactionType(TransactionType.TRANSFER);
        transactionRepository.save(transactionFromAccount);

        Transaction transactionToAccount = new Transaction();
        transactionToAccount.setAccountId(toAccount.getId());
        transactionToAccount.setAmount(transferFoundDto.amount());
        transactionToAccount.setTransactionTime(LocalDateTime.now());
        transactionToAccount.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(transactionToAccount);
    }

    public List<TransactionDto> getAccountAllTransaction(Long id) {
        accountRepository.findById(id).orElseThrow(() -> new AccountException("Account not found"));
        List<Transaction> transactions = transactionRepository.findAllByAccountIdOrderByTransactionTimeDesc(id);
        return transactions.stream()
                .map(TransactionMapper::toDto)
                .toList();
    }
}
