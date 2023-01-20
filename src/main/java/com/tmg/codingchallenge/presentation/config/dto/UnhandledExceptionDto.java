package com.tmg.codingchallenge.presentation.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnhandledExceptionDto {

    @NotBlank
    private String message;

    @NotBlank
    private HttpStatus httpStatus;

    @NotBlank
    private String uriOrigin;

    @NotBlank
    private String logMessage;

}
