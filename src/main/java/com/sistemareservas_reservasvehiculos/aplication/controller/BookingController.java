package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.sistemareservas_reservasvehiculos.aplication.service.AuthenticationService;
import com.sistemareservas_reservasvehiculos.domain.dto.BookingDto;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
public class BookingController{

    // Inyección de dependencias para el servicio de reservas y autenticación
    private final BookingService bookingService;
    private final AuthenticationService authenticationService;

    // Endpoint para crear una nueva reserva
    @PostMapping("/create")
    // Este método requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> registerBooking(@RequestBody @Valid BookingDto bookingDto) throws BookingException {
        // Obtiene el ID del usuario autenticado para asociarlo a la reserva
        int userId = authenticationService.idUser();
        // Registra la nueva reserva con los datos proporcionados y el ID del usuario
        bookingService.registerBooking(bookingDto, userId);
        // Retorna una respuesta con estado HTTP 201 (CREADO)
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para obtener todas las reservas con paginación, restringido a usuarios con el rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all/{offset}/{limit}")
    // Este método requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findAllBooking(@PathVariable("offset") Integer offset, @PathVariable("limit") Integer limit) throws BookingException {
        // Obtiene una lista de todas las reservas aplicando paginación
        List<BookingDto> bookings = bookingService.findAllBooking(offset, limit);
        // Retorna la lista de reservas con estado HTTP 200 (OK)
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // Endpoint para buscar una reserva por su ID
    @GetMapping("/search/{id}")
    // Este método requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findBookingById(@PathVariable("id") Integer id) throws BookingException {
        // Busca una reserva por su ID
        BookingDto booking = bookingService.findBookingById(id);
        // Retorna la reserva encontrada con estado HTTP 200 (OK)
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // Endpoint para actualizar una reserva existente por su ID
    @PutMapping("/update/{id}")
    // Este método requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> editBooking(@PathVariable("id") Integer id, @RequestBody BookingDto bookingDto) throws BookingException {
        // Actualiza la reserva existente con los nuevos datos proporcionados
        bookingService.editBooking(id, bookingDto);
        // Retorna una respuesta con estado HTTP 204 (NO CONTENT) indicando que la actualización fue exitosa pero no retorna contenido
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para eliminar una reserva por su ID
    @DeleteMapping("/delete/{id}")
    // Este método requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> removeBooking(@PathVariable("id") Integer id) throws BookingException {
        // Elimina la reserva especificada por su ID
        bookingService.removeBooking(id);
        // Retorna una respuesta con estado HTTP 204 (NO CONTENT) indicando que la eliminación fue exitosa pero no retorna contenido
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
