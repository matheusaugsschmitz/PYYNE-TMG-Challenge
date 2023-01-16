package com.tmg.codingchallenge.global.converter;


import com.tmg.codingchallenge.global.dto.ArgumentErrorDto;
import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArgumentErrorConverter {
    public static ArgumentErrorDto fromConstraintViolation(ConstraintViolation<?> constraintViolation) {
        String fieldName = constraintViolation.getPropertyPath().toString().replaceAll("^.*\\.(.*)$", "$1");
        return new ArgumentErrorDto(fieldName, Optional.ofNullable(constraintViolation.getMessage()).orElse(""));
    }
}
