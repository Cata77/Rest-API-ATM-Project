package com.restapi.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handlerUserAlreadyExists(UserAlreadyExistsException exception, WebRequest request) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFound(UserNotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<Object> handlerLowBalance(LowBalanceException exception, WebRequest request) {
        return new ResponseEntity<>(new ApiError(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
