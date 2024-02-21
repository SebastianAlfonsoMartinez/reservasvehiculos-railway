package com.sistemareservas_reservasvehiculos.aplication.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.mapper.UserMapper;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserOutDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserOutDto userOutDto;

    @BeforeEach
    void setUp() {
        user = new User(1, "John", "Doe", "password"
                , "john.doe@example.com", "1234567890", true, Set.of(ERole.USER), null);
        userDto = new UserDto(1, "John", "Doe", "password",
                "john.doe@example.com", "1234567890", true, null,
                Set.of(ERole.USER));
        userOutDto = new UserOutDto(1, "John", "Doe",
                "john.doe@example.com", "1234567890", true, null,
                Set.of(ERole.USER));
    }

    @Test
    void createUser() {
        when(mapper.toEntity(any(UserDto.class))).thenReturn(user);

        userService.createUser(userDto);

        verify(userRepository, times(1)).save(user);
    }


//    @Test
//    void userList() throws BookingException {
//        List<User> users = Arrays.asList(user);
//        Page<User> userPage = new PageImpl<>(users);
//
//        List<UserOutDto> userDtos = Arrays.asList(userOutDto);
//
//        when(userRepository.findAll(PageRequest.of(0, 5))).thenReturn(userPage);
//        // Usa anyList() para evitar problemas de igualdad de objetos
//        when(mapper.toDtoList(anyList())).thenReturn(userDtos);
//
//        List<UserOutDto> result = userService.userList(0, 5);
//
//        assertEquals(userDtos.size(), result.size(), "El tamaño de la lista de UserOutDto devuelta debe ser igual al esperado");
//        verify(userRepository).findAll(PageRequest.of(0, 5));
//        verify(mapper).toDtoList(anyList());
//    }

    @Test
    void userList_noUsersFound_throwsBookingException() {
        int offset = 0;
        int limit = 5;
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());

        when(userRepository.findAll(PageRequest.of(offset, limit))).thenReturn(emptyPage);

        assertThrows(BookingException.class, () -> {
            userService.userList(offset, limit);
        });
    }


    @Test
    void findUserById() throws BookingException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.toDtoOut(user)).thenReturn(userOutDto); // Corregido según el nombre correcto del método

        UserOutDto result = userService.findUserById(1);

        assertEquals(userOutDto.email(), result.email()); // Usar userOutDto para la comparación
        verify(userRepository, times(1)).findById(1);
        verify(mapper, times(1)).toDtoOut(user); // Asegurarse de que se verifica el método correcto
    }
    @Test
    void findUserById_userNotFound_throwsBookingException() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> {
            userService.findUserById(userId);
        });
    }

    @Test
    void deleteUser() throws BookingException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).delete(user);
    }
    @Test
    void deleteUser_userNotFound_throwsBookingException() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> {
            userService.deleteUser(userId);
        });
    }


    @Test
    void updateUser() throws BookingException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.toEntity(any(UserDto.class))).thenReturn(user);

        userService.updateUser(1, userDto);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_userNotFound_throwsBookingException() {
        int userId = 1;
        UserDto userDto = new UserDto(1, "John", "Doe", "password"
                , "johndoe@example.com", "1234567890", true, null,
                Set.of(ERole.USER));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> {
            userService.updateUser(userId, userDto);
        });
    }

}
