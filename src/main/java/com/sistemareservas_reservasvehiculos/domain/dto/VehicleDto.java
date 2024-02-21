package com.sistemareservas_reservasvehiculos.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VehicleDto (
        Integer id,
        String brand,
        String reference,
        String typeVehicle,
        String manufactureYear,
        String color,
        String typeTransmission,
        String numberDoors,
        String typeFuel,
        String imageUrl,
        Double price,
        Boolean available
){
}
