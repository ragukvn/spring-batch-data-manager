package com.ragukvn.data.manager.advise;

import com.ragukvn.data.manager.model.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<BaseErrorResponse> handleException(PropertyReferenceException ex) {
        log.error("Invalid property defined in filter: {}", ex.getMessage(), ex);
        return ResponseEntity
                .badRequest()
                .body(BaseErrorResponse.builder()
                        .title("Invalid Property Reference in query parameters")
                        .errors(List.of(String.format("Invalid property reference: %s", ex.getPropertyName())))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .badRequest().body(BaseErrorResponse.builder()
                        .title("Validation Failed")
                        .errors(ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                .toList())
                        .build());
    }
}
