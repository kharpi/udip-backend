package com.example.appointment_booking.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class CustomExceptionHandlerController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> resourceNotFoundException(HttpServletResponse res, CustomException ex) {

        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());


    }
}

