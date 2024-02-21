package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.service.VehicleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController{

    // Servicio para operaciones relacionadas con vehículos, inyectado en el controlador
    private final VehicleService vehicleService;

    // Método para crear un nuevo vehículo, accesible solo por usuarios con rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDto vehicleDto) {
        // Llama al servicio para crear un vehículo con los datos proporcionados
        vehicleService.createVehicle(vehicleDto);
        // Retorna una respuesta con estado HTTP 201 (CREADO)
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Método para obtener una lista de todos los vehículos con paginación
    @GetMapping("/all/{offset}/{limit}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> searchAll(@PathVariable("offset") Integer offset, @PathVariable("limit") Integer limit) throws BookingException {
        // Obtiene la lista de vehículos utilizando el servicio
        List<VehicleDto> vehicles = vehicleService.vehicleList(offset, limit);
        // Retorna la lista de vehículos con estado HTTP 200 (OK)
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    // Método para buscar un vehículo específico por su ID
    @GetMapping("/search/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> searchVehicle(@PathVariable("id") Integer id) throws BookingException {
        // Busca el vehículo por ID y retorna los datos encontrados
        return new ResponseEntity<>(vehicleService.findVehicleById(id), HttpStatus.OK);
    }

    // Método para eliminar un vehículo por su ID, accesible solo por usuarios con rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Integer id) throws BookingException {
        // Elimina el vehículo especificado utilizando el servicio
        vehicleService.deleteVehicle(id);
        // Retorna una respuesta con estado HTTP 204 (NO CONTENT) indicando éxito en la eliminación
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Método para actualizar la información de un vehículo por su ID, accesible solo por usuarios con rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Integer id, @RequestBody VehicleDto vehicleDto) throws BookingException {
        // Actualiza el vehículo con los nuevos datos proporcionados
        vehicleService.updateVehicle(id, vehicleDto);
        // Retorna una respuesta con estado HTTP 200 (OK) indicando éxito en la actualización
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
