package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserOutDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.mapper.UserMapper;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service

public record UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserMapper mapper
) {

    public void createUser(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        userRepository.save(user);
    }

    public List<UserOutDto> userList(Integer offset, Integer limit) throws BookingException {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<User> breweries = userRepository.findAll(pageable);
        if (breweries.getContent().isEmpty()) {
            throw new BookingException(EMessage.DATA_NOT_FOUND);
        }
        return mapper.toDtoListOut(breweries.getContent());
    }

    public UserOutDto findUserById(Integer id) throws BookingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND));
        return mapper.toDtoOut(user);
    }
    public void deleteUser(Integer id) throws BookingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND));
        userRepository.delete(user);
    }

    public void updateUser(Integer id, UserDto userDto) throws BookingException {
        userRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND));
        User user = mapper.toEntity(userDto);
        userRepository.save(user);
    }
    public void updateUserRoles(Integer userId, Set<ERole> newRoles) throws BookingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND));

        // Asegúrate de que el rol USER siempre esté presente
        newRoles.add(ERole.USER); // Esto garantiza que el rol USER siempre se incluya

        // Establece los nuevos roles
        user.setRoles(newRoles);

        userRepository.save(user);
    }

}

