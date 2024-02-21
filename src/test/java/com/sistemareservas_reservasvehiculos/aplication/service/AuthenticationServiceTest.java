package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.domain.dto.AuthenticationDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final UserDto sampleUserDto = new UserDto(
            2, // id
            "Jane", // firstName
            "Doe", // lastName
            "password123", // password
            "jane.doe@example.com", // email
            "1234567890", // phone
            true, // enable
            null,
            Set.of(ERole.USER) // role
    );

    private final AuthenticationDto sampleAuthenticationDto = new AuthenticationDto(
            "jane.doe@example.com", "password123"
    );

    private final User sampleUser = new User(
            2, // id
            "Jane", // firstName
            "Doe", // lastName
            "password123", // password
            "jane.doe@example.com", // email
            "1234567890", // phone
            true, // enable
            Set.of(ERole.USER), // role
            null // bookings (asumiendo que hay una relación con Booking)
    );

    @BeforeEach
    void setUp() {
        // Optional setup...
    }

    @Test
    void register_success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        String result = authenticationService.register(sampleUserDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void authenticate_success() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(sampleUser));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        String result = authenticationService.authenticate(sampleAuthenticationDto);

        assertNotNull(result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

}