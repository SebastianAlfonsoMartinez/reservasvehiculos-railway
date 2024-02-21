package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingExceptionHandler;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDetailDto;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BillDetailControllerTest {
    @Autowired
    private BillDetailController billDetailController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(billDetailController)
                .setControllerAdvice(new BookingExceptionHandler())
                .build();
    }
    private final VehicleDto sampleVehicleDto = new VehicleDto(
            null, // id
            "Toyota", // marca
            "Corolla", // referencia
            "Sedan", // tipo de vehículo
            "2021", // año de fabricación
            "Negro", // color
            "Automático", // tipo de transmisión
            "4", // número de puertas
            "Gasolina", // tipo de combustible
            "urlImagen", // URL de imagen
            20000.0, // precio
            true // disponible
    );

    // Ejemplo de parámetros para UserDto
    private final UserDto sampleUserDto = new UserDto(
            null, // id
            "Jane", // firstName
            "Doe", // lastName
            "password123", // password
            "jane.doe@example.com", // email
            "1234567890", // phone
            true,
            null,// enable
            Set.of(ERole.USER) // role
    );

    private final BillDto sampleBillDto = new BillDto(
            1, // ID existente en la base de datos
            ZonedDateTime.now(), // issuedDate
            250.0, // totalPrice
            sampleVehicleDto, // vehicle
            sampleUserDto // user
    );

    // Ejemplo de parámetros para BillDetailDto
    private final BillDetailDto sampleBillDetailDto = new BillDetailDto(
            null, // id
            "Service Charge", // description
            100.0, // amount
            sampleBillDto // bill
    );

//    @Test
//    void createBillDetail_success() throws Exception {
//        String billDetailDtoJson = objectMapper.writeValueAsString(sampleBillDetailDto);
//
//        mockMvc.perform(post("/api/v1/bill-detail/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(billDetailDtoJson))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void findAllBillsDetail_success() throws Exception {
//        mockMvc.perform(get("/api/v1/bill-detail/all/0/10"))
//                .andExpect(status().isFound());
//    }
//
//    @Test
//    void findBillDetailById_success() throws Exception {
//        // Asume que existe un detalle de factura con id 1
//        mockMvc.perform(get("/api/v1/bill-detail/search/2"))
//                .andExpect(status().isFound());
//    }

    @Test
    void findBillDetailById_NotFound() throws Exception {
        int nonExistingId = 999;
        mockMvc.perform(get("/api/v1/bill-detail/search/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteBillDetail_NotFound() throws Exception {
        int nonExistingId = 999;
        mockMvc.perform(delete("/api/v1/bill-detail/delete/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateBillDetail_NotFound() throws Exception {
        int nonExistingId = 999;
        String billDetailDtoJson = objectMapper.writeValueAsString(sampleBillDetailDto);

        mockMvc.perform(put("/api/v1/bill-detail/update/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(billDetailDtoJson))
                .andExpect(status().isNotFound());
    }

}