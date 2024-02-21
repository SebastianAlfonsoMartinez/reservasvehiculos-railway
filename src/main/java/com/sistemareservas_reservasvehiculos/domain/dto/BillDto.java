package com.sistemareservas_reservasvehiculos.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BillDto(
        Integer id,
        ZonedDateTime issuedDate,
        Double totalPrice,
        VehicleDto vehicle,
        UserDto user
) {
}
