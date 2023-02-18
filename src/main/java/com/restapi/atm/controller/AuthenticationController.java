package com.restapi.atm.controller;

import com.restapi.atm.dto.AuthenticatedUserDto;
import com.restapi.atm.dto.UserDto;
import com.restapi.atm.exception.ApiError;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The user is registered successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "id": 1,
                                              "userName": "testUser",
                                              "password": "pass123"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user already exists",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User already exists!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-17T14:26:11.9555656"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<UserDto> registerUser(@RequestBody AuthenticatedUserDto bankUser) {
        BankUser registeredBankUser = userService.registerUser(bankUser);

        UserDto userDto = modelMapper.map(registeredBankUser, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/v1/login")
    @Operation(
            tags = {"Authentication"},
            description = "This endpoint authenticates a user. If the user logs in with wrong credentials" +
                    " , an error will occur with a suggestive message.",
            responses = {@ApiResponse(responseCode = "200",
                    description = "The user is logged in successfully",
                    content = @Content(schema = @Schema(implementation = BankUser.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "userName": "testUser",
                                      "password": "pass123"
                                    }
                                    """))),
                    @ApiResponse(responseCode = "404",
                            description = "The credentials are incorrect",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "Bad credentials!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-17T15:08:13.27227"
                                            }
                                            """)))}
    )
    public ResponseEntity<UserDto> loginUser(@RequestBody AuthenticatedUserDto bankUser) {
        BankUser loggedBankUser = userService.loginUser(bankUser);

        UserDto userDto = modelMapper.map(loggedBankUser, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
