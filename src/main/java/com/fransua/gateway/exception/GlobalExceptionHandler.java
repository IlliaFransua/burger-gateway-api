package com.fransua.gateway.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, HttpServletRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, "Not Found", e.getMessage(), request);
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<ErrorResponse> handleResourceAccessException(
      ResourceAccessException e, HttpServletRequest request) {
    return buildResponse(
        HttpStatus.BAD_GATEWAY,
        "Bad gateway",
        "One of the internal services is unavailable",
        request);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException e, HttpServletRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, "Not Found", "Path not supported", request);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourcseFoundException(
      NoResourceFoundException e, HttpServletRequest request) {
    return buildResponse(
        HttpStatus.NOT_FOUND, "Not Found", "Requested resource not found", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobal(Exception e, HttpServletRequest request) {
    log.error("Unexpected error at {}: ", request.getRequestURI(), e);
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong", request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String error, String message, HttpServletRequest request) {
    ErrorResponse response =
        new ErrorResponse(Instant.now(), status.value(), error, message, request.getRequestURI());
    return new ResponseEntity<>(response, status);
  }
}
