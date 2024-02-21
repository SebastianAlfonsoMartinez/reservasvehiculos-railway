package com.sistemareservas_reservasvehiculos.aplication.mapper;

import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserOutDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.aplication.mapper.base.IBaseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends IBaseMapper {

    User toEntity(UserDto dto);

    UserDto toDto(User entity);

    List<User> toEntityList(List<UserDto> dtoList);

    List<UserOutDto> toDtoList(List<User> entityList);



    UserOutDto toDtoOut(User entity);
    List<UserOutDto> toDtoListOut(List<User> entityList);


}
