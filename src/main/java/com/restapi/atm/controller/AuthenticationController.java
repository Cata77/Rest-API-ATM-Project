package com.restapi.atm.controller;

import com.restapi.atm.dto.RegisteredUserDto;
import com.restapi.atm.dto.UserDto;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public AuthenticationController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping("/v1/register")
    @Operation(
            tags = {"Authentication"},
            description = "This endpoint registers a new user. If the user choose a username that" +
                    " already exists, an error will occur with a suggestive message.",
            responses = {@ApiResponse(responseCode = "200",
                         description = "The user is registered successfully")}
    )
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisteredUserDto bankUser) {
        BankUser registeredBankUser = userService.registerUser(bankUser);

        UserDto userDto = modelMapper.map(registeredBankUser, UserDto.class);

        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody BankUser bankUser) {
        BankUser loggedBankUser = userService.loginUser(bankUser);

        UserDto userDto = modelMapper.map(loggedBankUser, UserDto.class);

        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
