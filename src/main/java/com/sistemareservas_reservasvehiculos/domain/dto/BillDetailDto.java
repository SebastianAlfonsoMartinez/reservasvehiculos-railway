package com.sistemareservas_reservasvehiculos.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BillDetailDto(
    Integer id,
    String description,
    Double amount,
    BillDto bill
) {
}
