package com.sistemareservas_reservasvehiculos.aplication.lasting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EMessage {

  DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "The data was not found"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "The user was not found"),
  USER_EXISTS(HttpStatus.ALREADY_REPORTED, "The email was registered into the application previously"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "The email or password send are invalid");



  private final HttpStatus status;
  private final String message;


}
