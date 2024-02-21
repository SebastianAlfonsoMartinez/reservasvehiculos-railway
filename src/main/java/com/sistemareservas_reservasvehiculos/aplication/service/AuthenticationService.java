package com.sistemareservas_reservasvehiculos.aplication.service;


import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.domain.dto.AuthenticationDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public record AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager

) {

    public String register(UserDto userDto){
        Set<ERole> defaultRoles = new HashSet<>();
        defaultRoles.add(ERole.USER);
        User user = User.builder()
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .enable(true)
                .password(passwordEncoder.encode(userDto.password()))
                .phone(userDto.phone())
                .roles(defaultRoles)
                .enable(true)
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(AuthenticationDto authenticationDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.email(),
                        authenticationDto.password()
                )
        );
        User user = userRepository.findUserByEmail(authenticationDto.email()).orElseThrow();
        return jwtService.generateToken(user);
    }

    //Metodo para extraer el id usuario del contexto de seguridad.
    public Integer idUser () throws BookingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof UsernamePasswordAuthenticationToken jwtAuthToken)) {
            throw new BookingException(EMessage.INVALID_CREDENTIALS);
        }
        User principal = (User) jwtAuthToken.getPrincipal();
        return principal.getId();
    }

}
