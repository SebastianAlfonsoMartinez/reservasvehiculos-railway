package com.sistemareservas_reservasvehiculos.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EState;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDto(
        Integer id,

        LocalDateTime startDate,
        LocalDateTime endDate,
        EState state,

        VehicleDto vehicle,

        UserDto user
) {
}
