package com.restapi.atm.controller;

import com.restapi.atm.dto.AccountDto;
import com.restapi.atm.dto.BankUserDto;
import com.restapi.atm.exception.ApiError;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.service.AtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/bank")
public class AtmController {

    private final ModelMapper modelMapper;
    private final AtmService atmService;

    public AtmController(ModelMapper modelMapper, AtmService atmService) {
        this.modelMapper = modelMapper;
        this.atmService = atmService;
    }

    @GetMapping("/users")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the list of all bank users.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The list of bank users has been generated successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "username": "user1"
                                                },
                                                {
                                                    "id": 2,
                                                    "username": "user2"
                                                },
                                                {
                                                    "id": 3,
                                                    "username": "user3"
                                                }
                                            ]
                                            """)))
            }
    )
    public ResponseEntity<List<BankUserDto>> getUsersList() {
        List<BankUser> bankUserList = atmService.getAllUsers();

        List<BankUserDto> bankUserDtoList = bankUserList.stream()
                .map(user -> modelMapper.map(user, BankUserDto.class))
                .toList();

        return new ResponseEntity<>(bankUserDtoList, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the bank account of each user.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The list of bank accounts has been generated successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            [
                                                 {
                                                     "id": 3,
                                                     "balance": 50.00,
                                                     "transactions": [
                                                         {
                                                             "id": 3,
                                                             "timestamp": "2023-01-09T14:48:09.97302",
                                                             "value": 100.00,
                                                             "transactionType": "DEPOSIT"
                                                         },
                                                         {
                                                             "id": 6,
                                                             "timestamp": "2023-01-09T14:51:41.597675",
                                                             "value": 20.00,
                                                             "transactionType": "TRANSFER"
                                                         }
                                                     ]
                                                 },
                                                 {
                                                     "id": 8,
                                                     "balance": 0.00,
                                                     "transactions": []
                                                 },
                                                 {
                                                     "id": 9,
                                                     "balance": 0.00,
                                                     "transactions": []
                                                 },
                                                 {
                                                     "id": 6,
                                                     "balance": 280.00,
                                                     "transactions": [
                                                         {
                                                             "id": 10,
                                                             "timestamp": "2023-02-18T13:39:32.034074",
                                                             "value": 150.00,
                                                             "transactionType": "DEPOSIT"
                                                         },
                                                         {
                                                             "id": 11,
                                                             "timestamp": "2023-02-18T13:47:41.758348",
                                                             "value": 150.00,
                                                             "transactionType": "DEPOSIT"
                                                         },
                                                         {
                                                             "id": 14,
                                                             "timestamp": "2023-02-18T14:32:40.384778",
                                                             "value": 20.00,
                                                             "transactionType": "TRANSFER"
                                                         }
                                                     ]
                                                 }
                                             ]
                                            """)))
            }
    )
    public ResponseEntity<List<AccountDto>> getUsersAccountList() {
        List<Account> accountList = atmService.getAllUsersAccount();

        List<AccountDto> accountDtoList = accountList.stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();

        return new ResponseEntity<>(accountDtoList, HttpStatus.OK);
    }

    @GetMapping("/transactions")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the list of all bank transactions.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The list of bank transactions has been generated successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            [
                                                 {
                                                     "id": 1,
                                                     "timestamp": "2023-01-09T14:47:54.073484",
                                                     "value": 10.00,
                                                     "transactionType": "DEPOSIT",
                                                     "fromIdAccount": null,
                                                     "toIdAccount": null
                                                 },
                                                 {
                                                     "id": 2,
                                                     "timestamp": "2023-01-09T14:48:01.207005",
                                                     "value": 50.00,
                                                     "transactionType": "DEPOSIT",
                                                     "fromIdAccount": null,
                                                     "toIdAccount": null
                                                 },
                                                 {
                                                     "id": 3,
                                                     "timestamp": "2023-01-09T14:48:09.97302",
                                                     "value": 100.00,
                                                     "transactionType": "DEPOSIT",
                                                     "fromIdAccount": null,
                                                     "toIdAccount": null
                                                 }
                                            ]
                                            """)))
            }
    )
    public ResponseEntity<List<Transaction>> getTransactionList() {
        List<Transaction> accountList = atmService.getAllTransactions();

        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/highest-balance")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the account with the highest balance.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The account with the highest balance was found successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                 "id": 6,
                                                 "balance": 280.00,
                                                 "transactions": [
                                                     {
                                                         "id": 10,
                                                         "timestamp": "2023-02-18T13:39:32.034074",
                                                         "value": 150.00,
                                                         "transactionType": "DEPOSIT"
                                                     },
                                                     {
                                                         "id": 11,
                                                         "timestamp": "2023-02-18T13:47:41.758348",
                                                         "value": 150.00,
                                                         "transactionType": "DEPOSIT"
                                                     },
                                                     {
                                                         "id": 14,
                                                         "timestamp": "2023-02-18T14:32:40.384778",
                                                         "value": 20.00,
                                                         "transactionType": "TRANSFER"
                                                     }
                                                 ]
                                             }
                                            """)))
            }
    )
    public ResponseEntity<AccountDto> getAccountWithHighestBalance() {
        Account account = atmService.getHighestAccountBalance();

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @GetMapping("/balance")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the current bank's balance.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The current bank's balance was printed successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            500.00
                                            """)))
            }
    )
    public ResponseEntity<BigDecimal> getBankBalance() {
        BigDecimal balance = atmService.calculateBankBalance();

        return new ResponseEntity<>(balance,HttpStatus.OK);
    }

    @GetMapping("/user-most-transactions")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint shows the bank user with the most transactions done. If such user is not found" +
                    ", an error will occur with a suggestive message.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The user is found successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 2,
                                                "username": "user2"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user is not found",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User not found!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-17T14:26:11.9555656"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<BankUserDto> getBankUserWithMostTransactions() {
        BankUser bankUser = atmService.findUserWithMostTransactions();

        BankUserDto bankUserDto = modelMapper.map(bankUser, BankUserDto.class);

        return new ResponseEntity<>(bankUserDto,HttpStatus.OK);
    }

    @GetMapping("/date-most-transactions")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint prints the date in which most transactions have been done.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The date is generated successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            "2023-01-09"
                                            """)))
            }
    )
    public ResponseEntity<Date> getDateUserWithMostTransactions() {
        Date date = atmService.findDateWithMostTransactions();

        return new ResponseEntity<>(date,HttpStatus.OK);
    }

    @GetMapping("/transactions-between-dates")
    @Operation(
            tags = {"Bank"},
            description = "This endpoint prints a list of transactions that occurred between two given dates.",
            parameters = {@Parameter(name = "start", description = "The starting date (yyyy-MM-dd)", example = "2023-02-05"),
                    @Parameter(name = "end", description = "The ending date (yyyy-MM-dd)", example = "2023-02-06")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The bank list of transactions has been generated successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            [
                                                  {
                                                      "id": 8,
                                                      "timestamp": "2023-02-05T14:48:44.462991",
                                                      "value": 100.00,
                                                      "transactionType": "DEPOSIT",
                                                      "fromIdAccount": null,
                                                      "toIdAccount": null
                                                  }
                                             ]
                                            """)))
            }
    )
    public ResponseEntity<List<Transaction>> getTransactionsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Transaction> transactions = atmService.getTransactionsBetweenDate(start.atStartOfDay(),end.atStartOfDay());

        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
}
