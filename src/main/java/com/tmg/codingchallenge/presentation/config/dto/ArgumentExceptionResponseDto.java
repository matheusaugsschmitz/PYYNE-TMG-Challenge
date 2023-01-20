package com.tmg.codingchallenge.presentation.config.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ArgumentExceptionResponseDto implements Serializable {

    private final String timestamp = LocalDateTime.now().toString();

    private List<ArgumentErrorDto> argumentsErrors;

    private String detail;

    public ArgumentExceptionResponseDto(String detail, List<ArgumentErrorDto> argumentsErrors) {
        this.detail = detail;
        this.argumentsErrors = argumentsErrors;
    }
}
