package com.lokesh.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class ErrorResponseDto {
    private String apiPath;
    private HttpStatus errorStatus;
    private String errorStatusMsg;
    private LocalDateTime errorTime;
}
