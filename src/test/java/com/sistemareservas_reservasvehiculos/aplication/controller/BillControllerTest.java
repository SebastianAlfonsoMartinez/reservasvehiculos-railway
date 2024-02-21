package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingExceptionHandler;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BillControllerTest {
    @Autowired
    private BillController billController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final ZonedDateTime sampleDateTime = ZonedDateTime.now();
    private final VehicleDto sampleVehicleDto = new VehicleDto(null, "Toyota", "Corolla"
            , "Sedan", "2021", "Negro", "Automático"
            , "4", "Gasolina", "urlImagen", 20000.0, true);
    private final UserDto sampleUserDto = new UserDto(null, "Jane", "Doe"
            , "password123", "jane.doe@example.com", "1234567890", true, null,
            Set.of(ERole.USER));
    private final Vehicle sampleVehicle = new Vehicle(null, "Toyota", "Corolla"
            , "Sedan", "2021", "Negro", "Automático"
            , "4", "Gasolina", "urlImagen", 20000.0, true, null);
    private final User sampleUser = new User(2, "Jane", "Doe", "password123"
            , "jane.doe@example.com", "1234567890", true,
            Set.of(ERole.USER), null);
    private final BillDto sampleBillDto = new BillDto(1, sampleDateTime, 100.0
            , sampleVehicleDto, sampleUserDto);

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(billController)
                .setControllerAdvice(new BookingExceptionHandler())
                .build();

        Bill sampleBill = new Bill(
                1, // id
                ZonedDateTime.now(), // issuedDate
                250.0, // totalPrice
                sampleUser, // user
                sampleVehicle, // vehicle
                null // details (se establecerán después)
        );

        List<BillDetail> sampleBillDetails = Collections.singletonList(
                new BillDetail(1, "Service Charge", 100.0, sampleBill)
        );

        // Ahora que sampleBillDetails está inicializado, actualiza sampleBill con estos detalles
        sampleBill.setDetails(sampleBillDetails);
    }


//    @Test
//    void createBill_success() throws Exception {
//        String billDtoJson = objectMapper.writeValueAsString(sampleBillDto);
//
//        mockMvc.perform(post("/api/v1/bill/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(billDtoJson))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void findAllBill_success() throws Exception {
//        mockMvc.perform(get("/api/v1/bill/all/0/5"))
//                .andExpect(status().isFound());
//    }
//
//    @Test
//    void findBillByID_success() throws Exception {
//        // Asume que existe una factura con id 1
//        mockMvc.perform(get("/api/v1/bill/search/1"))
//                .andExpect(status().isFound());
//    }


//    @Test
//    void updateBill_success() throws Exception {
//        BillDto billDto = sampleBillDto;
//        String billDtoJson = objectMapper.writeValueAsString(billDto);
//
//        // Asume que existe una factura para actualizar con id 1
//        mockMvc.perform(put("/api/v1/bill/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(billDtoJson))
//                .andExpect(status().isNotFound());
//    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void findAllBill_throwsBookingException() throws Exception {
        mockMvc.perform(get("/api/v1/bill/all/2000/2050"))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }

    @Test
    void findBillByID_throwsBookingException() throws Exception {
        mockMvc.perform(get("/api/v1/bill/search/999"))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteBill_throwsBookingException() throws Exception {
        mockMvc.perform(delete("/api/v1/bill/delete/999"))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateBill_throwsBookingException() throws Exception {
        String billDtoJson = objectMapper.writeValueAsString(sampleBillDto);

        mockMvc.perform(put("/api/v1/bill/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(billDtoJson))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }
}