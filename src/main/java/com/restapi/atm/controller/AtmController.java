package com.restapi.atm.controller;

import com.restapi.atm.dto.AccountDto;
import com.restapi.atm.dto.BankUserDto;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.AtmService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AtmController {

    private final ModelMapper modelMapper;
    private final AtmService atmService;

    public AtmController(ModelMapper modelMapper, AtmService atmService) {
        this.modelMapper = modelMapper;
        this.atmService = atmService;
    }

    @GetMapping("/bank/users")
    public ResponseEntity<List<BankUserDto>> getUsersList() {
        List<BankUser> bankUserList = atmService.getAllUsers();

        List<BankUserDto> bankUserDtoList = bankUserList.stream()
                .map(user -> modelMapper.map(user, BankUserDto.class))
                .toList();

        return new ResponseEntity<>(bankUserDtoList, HttpStatus.OK);
    }

    @GetMapping("/bank/accounts")
    public ResponseEntity<List<AccountDto>> getUsersAccountList() {
        List<Account> accountList = atmService.getAllUsersAccount();

        List<AccountDto> accountDtoList = accountList.stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();

        return new ResponseEntity<>(accountDtoList, HttpStatus.OK);
    }

    @GetMapping("/bank/highest-balance")
    public ResponseEntity<AccountDto> getAccountWithHighestBalance() {
        Account account = atmService.getHighestAccountBalance();

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }
}
