package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingExceptionHandler;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EState;
import com.sistemareservas_reservasvehiculos.domain.dto.BookingDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(bookingController)
                .setControllerAdvice(new BookingExceptionHandler())
                .build();
    }

    private final UserDto sampleUserDto = new UserDto(
            1, // id
            "John", // firstName
            "Doe", // lastName
            null, // password (no es necesario para esta prueba)
            "john.doe@example.com", // email
            "1234567890", // phone
            true,
            null,
            Set.of(ERole.USER)// role
    );

    private final VehicleDto sampleVehicleDto = new VehicleDto(
            1, // id
            "Marca", // brand
            "Modelo", // reference
            "Tipo", // typeVehicle
            "2021", // manufactureYear
            "Color", // color
            "Transmision", // typeTransmission
            "4", // numberDoors
            "Combustible", // typeFuel
            "urlImagen", // imageUrl
            20000.0, // price
            true // available
    );

    private final BookingDto sampleBookingDto = new BookingDto(
            1, // id
            LocalDateTime.parse("2023-12-30T15:00:00"), // startDate
            LocalDateTime.parse("2023-12-30T20:00:00"), // endDate
            EState.ACTIVE,
            sampleVehicleDto, // vehicle
            sampleUserDto // user
    );

//    @Test
//    void registerBooking_success() throws Exception {
//        String bookingDtoJson = objectMapper.writeValueAsString(sampleBookingDto);
//
//        mockMvc.perform(post("/api/v1/booking/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(bookingDtoJson))
//                .andExpect(status().isCreated());
//    }

//    @Test
//    @Transactional
//    @WithMockUser(roles = {"ADMIN"})
//    void findAllBooking_success() throws Exception {
//        mockMvc.perform(get("/api/v1/booking/all/0/10"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Transactional
//    void findBookingById_success() throws Exception {
//        // Asume que existe una reserva con id 1
//        mockMvc.perform(get("/api/v1/booking/search/1"))
//                .andExpect(status().isOk());
//    }

    @Test
    void findBookingById_NotFound() throws Exception {
        int nonExistingId = 999;
        mockMvc.perform(get("/api/v1/booking/search/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void editBooking_success() throws Exception {
//        String bookingDtoJson = objectMapper.writeValueAsString(sampleBookingDto);
//
//        // Asume que existe una reserva para editar con id 1
//        mockMvc.perform(put("/api/v1/booking/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(bookingDtoJson))
//                .andExpect(status().isNoContent());
//    }

    @Test
    void editBooking_NotFound() throws Exception {
        int nonExistingId = 999;
        String bookingDtoJson = objectMapper.writeValueAsString(sampleBookingDto);

        mockMvc.perform(put("/api/v1/booking/update/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingDtoJson))
                .andExpect(status().isNotFound());
    }


    @Test
    void removeBooking_NotFound() throws Exception {
        int nonExistingId = 999;
        mockMvc.perform(delete("/api/v1/booking/delete/" + nonExistingId))
                .andExpect(status().isNotFound());
    }
}