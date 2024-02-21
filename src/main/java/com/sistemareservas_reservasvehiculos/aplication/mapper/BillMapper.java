package com.sistemareservas_reservasvehiculos.aplication.mapper;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import com.sistemareservas_reservasvehiculos.aplication.mapper.base.IBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface BillMapper extends IBaseMapper {
    @Mapping(target = "user", ignore = true)
    Bill toEntity(BillDto dto);

    BillDto toDto(Bill entity);

    List<Bill> toEntityList(List<BillDto> dtoList);

    List<BillDto> toDtoList(List<Bill> entityList);
}
