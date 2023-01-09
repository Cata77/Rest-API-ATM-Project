package com.restapi.atm.controller;

import com.restapi.atm.dto.AccountDto;
import com.restapi.atm.dto.BasicTransactionDto;
import com.restapi.atm.dto.TransferTransactionDto;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class UserController {

    @Autowired
    private ModelMapper modelMapper;
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AccountDto> getUserDetails(@PathVariable String id) {
        Account account = userService.getUserDetails(Integer.parseInt(id));

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/user/deposit")
    public ResponseEntity<BasicTransactionDto> createUserDeposit(
            @RequestParam String id,
            @RequestParam String amount) {
        Transaction transaction = userService.createDepositTransaction(Integer.parseInt(id),new BigDecimal(amount));

        BasicTransactionDto basicTransactionDto = modelMapper.map(transaction, BasicTransactionDto.class);

        return new ResponseEntity<>(basicTransactionDto, HttpStatus.CREATED);
    }

    @PatchMapping("/user/withdraw")
    public ResponseEntity<BasicTransactionDto> createUserWithdraw(
            @RequestParam String id,
            @RequestParam String amount) {
        Transaction transaction = userService.createWithdrawTransaction(Integer.parseInt(id),new BigDecimal(amount));

        BasicTransactionDto basicTransactionDto = modelMapper.map(transaction, BasicTransactionDto.class);

        return new ResponseEntity<>(basicTransactionDto, HttpStatus.CREATED);
    }

    @PatchMapping("/user/transfer")
    public ResponseEntity<TransferTransactionDto> createUserTransfer(
            @RequestParam String idAccount1,
            @RequestParam String idAccount2,
            @RequestParam String amount) {
        Transaction transaction = userService.createTransferTransaction(Integer.parseInt(idAccount1),
                Integer.parseInt(idAccount2), new BigDecimal(amount));

        TransferTransactionDto transferTransactionDto = modelMapper.map(transaction, TransferTransactionDto.class);

        return new ResponseEntity<>(transferTransactionDto, HttpStatus.CREATED);
    }
}
