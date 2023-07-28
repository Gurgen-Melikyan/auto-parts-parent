package com.example.autopartsrest.exception;

import com.example.autopartscommon.entity.RestErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {
        RestErrorDto errorDto = RestErrorDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = UserUnauthorizedException.class)
    public ResponseEntity<Object> handleUserUnauthorizedException(Exception ex, WebRequest request) {
        RestErrorDto errorDto = RestErrorDto.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
        RestErrorDto errorDto = RestErrorDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}