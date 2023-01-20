package com.tmg.codingchallenge.presentation.config;

import com.tmg.codingchallenge.presentation.config.converter.ArgumentErrorConverter;
import com.tmg.codingchallenge.presentation.config.dto.ArgumentErrorDto;
import com.tmg.codingchallenge.presentation.config.dto.ArgumentExceptionResponseDto;
import com.tmg.codingchallenge.presentation.config.dto.BusinessExceptionResponseDto;
import com.tmg.codingchallenge.presentation.config.dto.UnhandledExceptionDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tmg.codingchallenge.presentation.config.helper.WebRequestHelper.getUriOrigin;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler used when a dto validation fails
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final List<ArgumentErrorDto> argumentsErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(field -> new ArgumentErrorDto(field.getField(), Optional.ofNullable(field.getDefaultMessage()).orElse("")))
                .collect(Collectors.toList());

        argumentsErrors.addAll(ex.getBindingResult()
                .getGlobalErrors().stream()
                .map(error -> new ArgumentErrorDto(error.getObjectName(), Optional.ofNullable(error.getDefaultMessage()).orElse("")))
                .toList()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getBodyArgumentsError(request, argumentsErrors));
    }

    /**
     * Handler used when type is mismatch
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(status)
                .body(new BusinessExceptionResponseDto(ex.getMessage(), getUriOrigin(request)));
    }

    /**
     * Handler used when a method parameter validation fails
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ArgumentExceptionResponseDto> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        final List<ArgumentErrorDto> argumentsErrors = exception.getConstraintViolations()
                .stream()
                .map(ArgumentErrorConverter::fromConstraintViolation)
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getBodyArgumentsError(request, argumentsErrors));
    }

    /**
     * Default handler. Used for unmapped exceptions.
     */
    @ExceptionHandler
    public ResponseEntity<BusinessExceptionResponseDto> handleAllExceptions(Exception exception, WebRequest request) {
        final String uriOrigin = getUriOrigin(request);
        final UnhandledExceptionDto unhandledExceptionDto = createDefaultUnhandledExceptionDto(uriOrigin);

        log.error(unhandledExceptionDto.getLogMessage(), exception);
        return ResponseEntity
                .status(unhandledExceptionDto.getHttpStatus())
                .body(new BusinessExceptionResponseDto(unhandledExceptionDto.getMessage(), unhandledExceptionDto.getUriOrigin()));
    }

    private static UnhandledExceptionDto createDefaultUnhandledExceptionDto(String uriOrigin) {
        return UnhandledExceptionDto.builder()
                .message("An unexpected error happened. Please contact support")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .uriOrigin(uriOrigin)
                .logMessage("Request error: unhandled error while processing request")
                .build();
    }

    public ArgumentExceptionResponseDto getBodyArgumentsError(WebRequest request, List<ArgumentErrorDto> argumentsErrors) {
        return new ArgumentExceptionResponseDto(getUriOrigin(request), argumentsErrors);
    }

}
