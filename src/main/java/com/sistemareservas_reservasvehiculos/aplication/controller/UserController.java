package com.sistemareservas_reservasvehiculos.aplication.controller;


import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.service.AuthenticationService;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.service.UserService;
import com.sistemareservas_reservasvehiculos.domain.dto.UserOutDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController{

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all/{offset}/{limit}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> searchAll(
            @PathVariable("offset") Integer offset,
            @PathVariable("limit") Integer limit) throws BookingException {
        List<UserOutDto> users = userService.userList(offset, limit);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/info")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> searchUser() throws BookingException {
        Integer userId = authenticationService.idUser();
        System.out.println(userId);
        return new  ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) throws BookingException {
        userService.deleteUser(id);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody UserDto userDto) throws BookingException {
        userService.updateUser(id, userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{userId}/roles")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateUserRoles(@PathVariable Integer userId, @RequestBody Set<ERole> newRoles) throws BookingException {
            userService.updateUserRoles(userId, newRoles);
            return new ResponseEntity<>(HttpStatus.OK);
        }
}

