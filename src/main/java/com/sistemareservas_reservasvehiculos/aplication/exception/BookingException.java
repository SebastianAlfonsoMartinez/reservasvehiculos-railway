package com.sistemareservas_reservasvehiculos.aplication.exception;

import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookingException extends Exception{
    private final HttpStatus status;
    private final String message;


    public BookingException(EMessage eMessage) {
        this.status = eMessage.getStatus();
        this.message = eMessage.getMessage();
    }
}