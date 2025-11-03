package com.example.banking.controller;

import com.example.banking.dto.AccountDto;
import com.example.banking.dto.TransactionDto;
import com.example.banking.dto.TransferFoundDto;
import com.example.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestParam double amount) {
        return new ResponseEntity<>(accountService.deposit(id, amount), HttpStatus.OK);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestParam double amount) {
        return new ResponseEntity<>(accountService.withdraw(id, amount), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFounds(@RequestBody TransferFoundDto transferFoundDto){
        accountService.transferFounds(transferFoundDto);
        return new ResponseEntity<>("Transferred", HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getAccountAllTransaction(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccountAllTransaction(id), HttpStatus.OK);
    }
}
