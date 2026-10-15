package com.tecylab.client_api.exception;

import com.tecylab.client_api.dto.ErrorDTO;
import com.tecylab.client_api.model.OperationLog;
import com.tecylab.client_api.repository.OperationLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private final OperationLogRepository operationLogRepository;

    // You can add exception handling methods here, for example:
     @ExceptionHandler(ClientNotFoundException.class)
     public ResponseEntity<ErrorDTO> handleClientNotFoundException(ClientNotFoundException ex) {
            ErrorDTO errorResponse = new ErrorDTO(
                    "404",
                    "Client Not Found",
                    ex.getMessage(),
                    java.time.LocalDateTime.now(),
                    "/clients/{id}"
            );
            operationLogRepository.save(
                    OperationLog.builder()
                            .operation("Error")
                            .description(ex.getMessage())
                            .timestamp(java.time.LocalDateTime.now())
                            .build()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
     }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO errorResponse = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Internal Server Error",
                e.getMessage(),
                java.time.LocalDateTime.now(),
                "/clients"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
