package com.sistemareservas_reservasvehiculos.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticationDto(
        String email,
        String password
) {
}
