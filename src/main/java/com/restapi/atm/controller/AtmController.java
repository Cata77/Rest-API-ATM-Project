package com.restapi.atm.controller;

import com.restapi.atm.dto.BankUserDto;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.AtmService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
