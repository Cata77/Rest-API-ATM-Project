package com.restapi.atm.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    @Schema(example = "Suggestive Error Message")
    private String message;

    @Schema(example = "Not Found!")
    private HttpStatus status;

    @Schema(example = "2023-02-17T14:26:11.9555656")
    private LocalDateTime time;
}
