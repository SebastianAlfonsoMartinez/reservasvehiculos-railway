package com.sistemareservas_reservasvehiculos.aplication.mapper;

import com.sistemareservas_reservasvehiculos.domain.dto.BookingDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Booking;
import com.sistemareservas_reservasvehiculos.aplication.mapper.base.IBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends IBaseMapper {
    @Mapping(target = "user", ignore = true)
    Booking toEntity(BookingDto dto);

    BookingDto toDto(Booking entity);

    List<Booking> toEntityList(List<BookingDto> dtoList);

    List<BookingDto> toDtoList(List<Booking> entityList);

}