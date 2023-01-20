package com.tmg.codingchallenge.presentation.config.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BusinessExceptionResponseDto implements Serializable {

    private final String timestamp = LocalDateTime.now().toString();

    private String message;

    private String detail;

    public BusinessExceptionResponseDto(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }

}
