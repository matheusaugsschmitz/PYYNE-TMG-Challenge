package com.tmg.codingchallenge.unittest.global.converter;

import com.tmg.codingchallenge.global.converter.ArgumentErrorConverter;
import com.tmg.codingchallenge.global.dto.ArgumentErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentErrorConverterTest {

    @Test
    void fromConstraintViolationWithOneLevelPath_withSuccess() {
        // Arrange
        String message = "Validation Message";
        String fieldName = "key";
        Path path = PathImpl.createPathFromString(fieldName);
        ConstraintViolation<?> constraintViolation = createConstraintViolation(message, path);

        // Act
        ArgumentErrorDto argumentErrorDto = ArgumentErrorConverter.fromConstraintViolation(constraintViolation);

        // Assert
        assertEquals(fieldName, argumentErrorDto.getField());
        assertEquals(message, argumentErrorDto.getMessage());
    }

    @Test
    void fromConstraintViolationWithTwoLevelsPath_withSuccess() {
        // Arrange
        String message = "Validation Message 2";
        String fieldName = "key";
        Path path = PathImpl.createPathFromString("dtoClass." + fieldName);
        ConstraintViolation<?> constraintViolation = createConstraintViolation(message, path);

        // Act
        ArgumentErrorDto argumentErrorDto = ArgumentErrorConverter.fromConstraintViolation(constraintViolation);

        // Assert
        assertEquals(fieldName, argumentErrorDto.getField());
        assertEquals(message, argumentErrorDto.getMessage());
    }

    private static ConstraintViolation<Object> createConstraintViolation(String message, Path path) {
        return ConstraintViolationImpl.forBeanValidation(null, null, null, message, null, null, null, null, path, null, null);
    }

}
