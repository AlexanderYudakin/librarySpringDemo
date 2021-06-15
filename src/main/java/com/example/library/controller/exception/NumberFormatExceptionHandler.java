package com.example.library.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class NumberFormatExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(NumberFormatException exception) {
        return exception.getMessage();
    }
}
