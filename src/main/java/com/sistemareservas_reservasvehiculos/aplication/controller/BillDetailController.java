package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDetailDto;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.service.BillDetailService;
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
@RequestMapping("api/v1/bill-detail")
@RequiredArgsConstructor
public class BillDetailController{

    // Inyección del servicio BillDetailService para realizar operaciones de negocio sobre los detalles de las facturas
    private final BillDetailService billDetailService;

    // Endpoint para crear un nuevo detalle de factura
    @PostMapping("/create")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createBillDetail(@RequestBody BillDetailDto billDetailDto) {
        // Llama al servicio para crear un detalle de factura con los datos proporcionados
        billDetailService.createBillDetail(billDetailDto);
        // Retorna una respuesta con el código de estado HTTP 201 (CREADO)
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los detalles de facturas con paginación, restringido a usuarios con el rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all/{offset}/{limit}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findAllBillsDetail(@PathVariable("offset") Integer offset, @PathVariable("limit") Integer limit) throws BookingException {
        // Obtiene una lista de todos los detalles de facturas aplicando paginación
        List<BillDetailDto> billDetailList = billDetailService.findAllBillDetail(offset, limit);
        // Retorna la lista de detalles de facturas con el código de estado HTTP 302 (ENCONTRADO)
        return new ResponseEntity<>(billDetailList, HttpStatus.FOUND);
    }

    // Endpoint para buscar un detalle de factura por su ID
    @GetMapping("/search/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> findBillDetailById(@PathVariable("id") Integer id) throws BookingException {
        // Busca un detalle de factura por su ID
        BillDetailDto billDetailDto = billDetailService.findBillDetailById(id);
        // Retorna el detalle de la factura encontrada con el código de estado HTTP 302 (ENCONTRADO)
        return new ResponseEntity<>(billDetailDto, HttpStatus.FOUND);
    }

    // Endpoint para eliminar un detalle de factura por su ID, restringido a usuarios con el rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteBillDetail(@PathVariable("id") Integer id) throws BookingException {
        // Elimina un detalle de factura por su ID
        billDetailService.deleteBillDetail(id);
        // Retorna una respuesta con el código de estado HTTP 404 (NO ENCONTRADO) si el recurso no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para actualizar un detalle de factura por su ID, restringido a usuarios con el rol ADMIN
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    // Requiere autenticación JWT para acceder a este método
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateBillDetail(@PathVariable("id") Integer id, BillDetailDto billDetailDto) throws BookingException {
        // Actualiza un detalle de factura existente con los nuevos datos proporcionados
        billDetailService.updateBillDetail(id, billDetailDto);
        // Retorna una respuesta con el código de estado HTTP 302 (ENCONTRADO) tras la actualización exitosa
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

}
