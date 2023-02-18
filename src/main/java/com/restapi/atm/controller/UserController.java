package com.restapi.atm.controller;

import com.restapi.atm.dto.AccountDto;
import com.restapi.atm.dto.BasicTransactionDto;
import com.restapi.atm.dto.TransactionDto;
import com.restapi.atm.exception.ApiError;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(
            tags = {"User"},
            description = "This endpoint shows the user's bank account details (current balance and it's transactions history). " +
                    "If the user ID is not found, an error will occur with a suggestive message.",
            parameters = {@Parameter(name = "id", description = "User ID", example = "3")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The user bank account details loads successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
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
                                             }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user ID is not found",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User not found!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-18T13:30:09.0634324"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<AccountDto> getUserDetails(@PathVariable String id) {
        Account account = userService.getUserDetails(Integer.parseInt(id));

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/deposit")
    @Operation(
            tags = {"User"},
            description = "This endpoint deposits money into a user's bank account. " +
                    "If the user's bank account is not found, an error will occur with a suggestive message.",
            parameters = {@Parameter(name = "id", description = "User ID", example = "6"),
                          @Parameter(name = "amount", description = "Amount of money to be deposited", example = "150")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The money has been deposited successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                  "id": 10,
                                                  "timestamp": "2023-02-18T13:39:32.034074",
                                                  "value": 150,
                                                  "transactionType": "DEPOSIT"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user ID is not found",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User not found!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-18T13:46:36.8041556"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<BasicTransactionDto> createUserDeposit(
            @RequestParam String id,
            @RequestParam String amount) {
        Transaction transaction = userService.createDepositTransaction(Integer.parseInt(id),new BigDecimal(amount));

        BasicTransactionDto basicTransactionDto = modelMapper.map(transaction, BasicTransactionDto.class);

        return new ResponseEntity<>(basicTransactionDto, HttpStatus.CREATED);
    }

    @PatchMapping("/withdraw")
    @Operation(
            tags = {"User"},
            description = "This endpoint withdraws money from a user's bank account. " +
                    "If the user's bank account is not found or if the amount to be withdrawn is higher than the " +
                    "user's bank account balance, an error will occur with a suggestive message.",
            parameters = {@Parameter(name = "id", description = "User ID", example = "2"),
                    @Parameter(name = "amount", description = "Amount of money to be withdrawn", example = "10")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The money has been deposited successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 13,
                                                "timestamp": "2023-02-18T13:58:30.3733184",
                                                "value": 10,
                                                "transactionType": "WITHDRAW"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user ID is not found",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User not found!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-18T13:46:36.8041556"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "406",
                            description = "The balance is too low to withdraw the amount of money",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "Insufficient funds!",
                                                "status": "NOT_ACCEPTABLE",
                                                "time": "2023-02-18T14:01:56.3154052"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<BasicTransactionDto> createUserWithdraw(
            @RequestParam String id,
            @RequestParam String amount) {
        Transaction transaction = userService.createWithdrawTransaction(Integer.parseInt(id),new BigDecimal(amount));

        BasicTransactionDto basicTransactionDto = modelMapper.map(transaction, BasicTransactionDto.class);

        return new ResponseEntity<>(basicTransactionDto, HttpStatus.CREATED);
    }

    @PatchMapping("/transfer")
    @Operation(
            tags = {"User"},
            description = "This endpoint transfers money between two bank accounts. " +
                    "If one of the bank account is not found or if the amount to be withdrawn is higher than the " +
                    "sender's bank account balance, an error will occur with a suggestive message.",
            parameters = {@Parameter(name = "idAccount1", description = "Sender User ID", example = "6"),
                    @Parameter(name = "idAccount2", description = "Beneficiary User ID", example = "2"),
                    @Parameter(name = "amount", description = "Amount of money to be transferred", example = "20")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The money has been transferred successfully",
                            content = @Content(schema = @Schema(implementation = BankUser.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "id": 14,
                                                "timestamp": "2023-02-18T14:32:40.3847779",
                                                "value": 20,
                                                "transactionType": "TRANSFER",
                                                "fromIdAccount": 6,
                                                "toIdAccount": 2
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404",
                            description = "The user ID is not found",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "User not found!",
                                                "status": "NOT_FOUND",
                                                "time": "2023-02-18T13:46:36.8041556"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "406",
                            description = "The sender's balance is too low to transfer the amount of money",
                            content = @Content(schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "message": "Insufficient funds!",
                                                "status": "NOT_ACCEPTABLE",
                                                "time": "2023-02-18T14:01:56.3154052"
                                            }
                                            """)))
            }
    )
    public ResponseEntity<TransactionDto> createUserTransfer(
            @RequestParam String idAccount1,
            @RequestParam String idAccount2,
            @RequestParam String amount) {
        Transaction transaction = userService.createTransferTransaction(Integer.parseInt(idAccount1),
                Integer.parseInt(idAccount2), new BigDecimal(amount));

        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);

        return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @GetMapping("/statement")
    public ResponseEntity<List<TransactionDto>> getBankStatementFromAPeriod(
            @RequestParam String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Transaction> transactions = userService.getBankStatement(Integer.parseInt(id), start.atStartOfDay(),end.atStartOfDay());

        List<TransactionDto> transactionDtoList = transactions.stream().
                map(tr -> modelMapper.map(tr, TransactionDto.class))
                .toList();

        return new ResponseEntity<>(transactionDtoList,HttpStatus.OK);
    }

    @DeleteMapping("/close-account/{id}")
    public ResponseEntity<String> closeBankAccount(@PathVariable String id) {
        userService.shotDownUserBankAccount(id);

        return new ResponseEntity<>("Bank account closed",HttpStatus.OK);
    }
}
