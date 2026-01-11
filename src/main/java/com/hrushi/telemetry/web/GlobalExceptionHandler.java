package com.hrushi.telemetry.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    ProblemDetail handleException(Exception e) {
        logger.error("Unhandled exception", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<@Nullable String> globalErrorMessages = ex.getGlobalErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        Map<String, List<@Nullable String>> fieldErrors = ex.getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content");
        problemDetail.setProperty("errors", Map.of("global", globalErrorMessages, "field", fieldErrors));
        return new ResponseEntity<>(problemDetail, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolation(ConstraintViolationException e) {
        Map<String, List<String>> parameterErrors = e.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(violation -> getParameterName(violation.getPropertyPath()), Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content");
        problemDetail.setProperty("errors", Map.of("parameter", parameterErrors));
        return problemDetail;
    }

    private String getParameterName(Path propertyPath) {
        String pathString = propertyPath.toString();
        int lastDotIndex = pathString.lastIndexOf('.');
        return lastDotIndex >= 0 ? pathString.substring(lastDotIndex + 1) : pathString;
    }
}
