package com.task.parking.exception;

import com.task.parking.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
  }

  @ExceptionHandler(InvalidParkingRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidParkingRequest(InvalidParkingRequestException ex) {
    log.warn("Invalid parking request: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null);
  }

  @ExceptionHandler(ParkingConflictException.class)
  public ResponseEntity<ErrorResponse> handleParkingConflict(ParkingConflictException ex) {
    log.warn("Parking conflict: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.warn("Validation failed for request");

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error",
        "Invalid request data", errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred: ", ex);
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
        "An unexpected error occurred", null);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error,
                                                           String message, Object details) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(error)
        .message(message)
        .details(details)
        .build();
    return ResponseEntity.status(status).body(response);
  }
}
