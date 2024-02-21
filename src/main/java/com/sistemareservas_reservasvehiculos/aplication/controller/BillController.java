package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.sistemareservas_reservasvehiculos.aplication.service.AuthenticationService;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.service.BillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    // Inyecta el servicio de facturación para operaciones relacionadas con facturas
    private final BillService billService;
    // Inyecta el servicio de autenticación para obtener información del usuario autenticado
    private final AuthenticationService authenticationService;

    // Endpoint para crear una nueva factura
    @PostMapping("/create")
    // Indica que este endpoint está protegido y requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createBill(@RequestBody BillDto billDto) throws BookingException {
        // Obtiene el ID del usuario autenticado
        int idUser = authenticationService.idUser();
        // Crea una nueva factura con los datos proporcionados
        billService.createBill(billDto, idUser);
        // Devuelve una respuesta con estado HTTP 201 (CREATED)
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para obtener todas las facturas, con acceso restringido a usuarios con rol de ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all/{offset}/{limit}")
    // Indica que este endpoint está protegido y requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findAllBill(@PathVariable("offset") Integer offset, @PathVariable("limit") Integer limit) throws BookingException {
        // Recupera una lista de facturas con paginación
        List<BillDto> bills = billService.findAllBill(offset, limit);
        // Devuelve la lista de facturas con estado HTTP 302 (FOUND)
        return new ResponseEntity<>(bills, HttpStatus.FOUND);
    }

    // Endpoint para buscar una factura por su ID
    @GetMapping("/search/{id}")
    // Indica que este endpoint está protegido y requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findBillByID(@PathVariable("id") Integer id) throws BookingException {
        // Busca una factura por su ID
        BillDto bill = billService.findBillById(id);
        // Devuelve la factura encontrada con estado HTTP 302 (FOUND)
        return new ResponseEntity<>(bill, HttpStatus.FOUND);
    }

    // Endpoint para eliminar una factura, con acceso restringido a usuarios con rol de ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    // Indica que este endpoint está protegido y requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteBill(@PathVariable("id") Integer id) throws BookingException {
        // Elimina una factura por su ID
        billService.deleteBill(id);
        // Devuelve una respuesta con estado HTTP 404 (NOT FOUND) si la factura no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para actualizar una factura, con acceso restringido a usuarios con rol de ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    // Indica que este endpoint está protegido y requiere autenticación JWT
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateBill(@PathVariable("id") Integer id, @RequestBody BillDto billDto) throws BookingException {
        // Actualiza una factura existente con los nuevos datos proporcionados
        billService.updateBill(id, billDto);
        // Devuelve una respuesta con estado HTTP 404 (NOT FOUND) si la factura a actualizar no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
