package com.sistemareservas_reservasvehiculos.aplication.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BookingExceptionHandler {


    @ExceptionHandler(BookingException.class)
    public ResponseEntity<?> handleBreweryTourException(BookingException exception, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, exception.getStatus());
    }
}