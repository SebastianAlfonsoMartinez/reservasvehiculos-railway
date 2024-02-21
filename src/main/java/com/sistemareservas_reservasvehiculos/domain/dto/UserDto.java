package com.sistemareservas_reservasvehiculos.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.domain.entity.Booking;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String password,
        @Email
        String email,
        String phone,
        Boolean enable,
        List<Booking> bookings,
        Set<ERole> roles
) {
}
