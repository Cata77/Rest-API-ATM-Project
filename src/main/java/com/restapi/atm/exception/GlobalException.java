package com.restapi.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handlerUserAlreadyExists(UserAlreadyExistsException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFound(UserNotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<Object> handlerLowBalance(LowBalanceException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE, LocalDateTime.now()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserTransactionsNotFoundException.class)
    public ResponseEntity<Object> handlerTransactionsNotFound(UserTransactionsNotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
