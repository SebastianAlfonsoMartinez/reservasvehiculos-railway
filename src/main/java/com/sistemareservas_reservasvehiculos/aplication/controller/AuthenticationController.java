package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.sistemareservas_reservasvehiculos.domain.dto.AuthenticationDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.aplication.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthenticationController(
        // Inyecta el servicio de autenticación para ser utilizado en los métodos del controlador
        AuthenticationService authenticationService
) {

  // Maneja las solicitudes POST a "/api/v1/auth/register", destinadas al registro de nuevos usuarios
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UserDto userDto) {
    // Invoca al servicio de autenticación para registrar un nuevo usuario y obtener un token
    String token = authenticationService.register(userDto);
    // Devuelve el token generado con un estado HTTP 201 (CREATED), indicando que el recurso fue creado exitosamente
    return new ResponseEntity<>(token, HttpStatus.CREATED);
  }

  // Maneja las solicitudes POST a "/api/v1/auth/authenticate", destinadas a la autenticación de usuarios
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(@RequestBody AuthenticationDto authenticationDto) {
    // Invoca al servicio de autenticación para autenticar al usuario y obtener un token
    String token = authenticationService.authenticate(authenticationDto);
    // Devuelve el token generado con un estado HTTP 200 (OK), indicando que la solicitud fue exitosa
    return new ResponseEntity<>(token, HttpStatus.OK);
  }
}
