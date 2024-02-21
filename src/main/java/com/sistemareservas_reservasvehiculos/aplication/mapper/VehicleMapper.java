package com.sistemareservas_reservasvehiculos.aplication.mapper;

import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.aplication.mapper.base.IBaseMapper;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface VehicleMapper extends IBaseMapper {

    Vehicle toEntity(VehicleDto dto);
    VehicleDto toDto(Vehicle entity);
    List<Vehicle> toEntityList(List<VehicleDto> dtoList);
    List<VehicleDto> toDtoList(List<Vehicle> entityList);

}
