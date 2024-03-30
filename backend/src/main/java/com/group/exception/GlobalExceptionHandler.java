package com.group.exception;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TaskValidateException.class)
    public ResponseEntity<AppException> handleValidateException(TaskValidateException e){
        logger.error(e.getMessage(),e);
        return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(),
                e.getMessage()),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<AppException> handleTaskNotFoundException(TaskNotFoundException e){
        logger.error(e.getMessage(),e);
        return new ResponseEntity<>(new AppException(HttpStatus.NOT_FOUND.value(),
                e.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppException> handleException(Exception e){
        logger.error(e.getMessage(),e);
        return new ResponseEntity<>(new AppException(HttpStatus.UNAUTHORIZED.value(),
                e.getMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AppException> handleValidateException(Exception e){
        logger.error(e.getMessage(),e);
        return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(),
                e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppException> validationException(ConstraintViolationException e){
        logger.error("Validation error occurred: {}", e.getMessage());
            List<String> validationErrors = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
        return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(),
                validationErrors.toString()),HttpStatus.BAD_REQUEST);
    }


}
