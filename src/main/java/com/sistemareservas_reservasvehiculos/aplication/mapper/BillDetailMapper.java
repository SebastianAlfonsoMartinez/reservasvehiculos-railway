package com.sistemareservas_reservasvehiculos.aplication.mapper;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDetailDto;
import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
import com.sistemareservas_reservasvehiculos.aplication.mapper.base.IBaseMapper;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface BillDetailMapper extends IBaseMapper {
    BillDetail toEntity(BillDetailDto dto);

    BillDetailDto toDto(BillDetail entity);

    List<BillDetail> toEntityList(List<BillDetailDto> dtoList);

    List<BillDetailDto> toDtoList(List<BillDetail> entityList);

}
