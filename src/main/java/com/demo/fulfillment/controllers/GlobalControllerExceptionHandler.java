package com.demo.fulfillment.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<String> handleAgentFailure(WebClientException exception) {
        logger.error("WebClientException: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> recordsNotFoundException(NoSuchElementException ex) {
        logger.error("NoSuchElementException: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handle(TransactionSystemException exception) {
        logger.error("TransactionSystemException: ", exception);
        ConstraintViolationException constraint = (ConstraintViolationException) exception.getRootCause();

        String message;
        if (constraint != null && constraint.getConstraintViolations() != null) {
            message = constraint.getConstraintViolations().iterator().next().getMessage();
        } else {
            message = exception.getMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException exception) {
        logger.error("MethodArgumentNotValidException: ", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getBindingResult()
                        .getAllErrors()
                        .get(0)
                        .getDefaultMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> alternateProcessingException(IOException ex) {
        logger.error("IOException: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Exception exception) {
        logger.error("Unhandled Exception: ", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}