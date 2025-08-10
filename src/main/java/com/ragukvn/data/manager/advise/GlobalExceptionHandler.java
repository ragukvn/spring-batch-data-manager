package com.ragukvn.data.manager.advise;

import com.ragukvn.data.manager.model.BaseErrorResponse;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
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
        log.error("Invalid property defined in filter: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(BaseErrorResponse.builder()
                        .title("Invalid Property Reference in query parameters")
                        .errors(List.of(String.format("Invalid property reference: %s", ex.getPropertyName())))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred: {}", ex.getMessage());
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Exception during request [{} {}]: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity
                .internalServerError()
                .body(BaseErrorResponse.builder().title("Something went wrong")
                        .errors(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<BaseErrorResponse> handleOptimisticLockException(OptimisticLockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseErrorResponse.builder()
                        .title("Optimistic Lock Exception")
                        .errors(List.of("The resource you are trying to update has been modified by another transaction. Please refresh and try again."))
                        .build());
    }
}
