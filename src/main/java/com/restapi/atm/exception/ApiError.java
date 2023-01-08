package com.restapi.atm.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private String message;
    private HttpStatus status;
    private LocalDateTime time;
}
