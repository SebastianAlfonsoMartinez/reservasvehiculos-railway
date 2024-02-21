package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingExceptionHandler;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VehicleControllerTest {

    @Autowired
    private VehicleController vehicleController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(vehicleController)
                .setControllerAdvice(new BookingExceptionHandler())
                .build();
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createVehicle_success() throws Exception {
        VehicleDto vehicleDto = new VehicleDto(
                null,
                "Toyota",
                "Corolla",
                "Sedan",
                "2021",
                "Negro",
                "Automático",
                "4",
                "Gasolina",
                "urlImagen",
                20000.0,
                true
        );
        String vehicleDtoJson = objectMapper.writeValueAsString(vehicleDto);

        mockMvc.perform(post("/api/v1/vehicle/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleDtoJson))
                .andExpect(status().isCreated());
    }


//    @Test
//    void searchAll_success() throws Exception {
//        mockMvc.perform(get("/api/v1/vehicle/all/0/10"))
//                .andExpect(status().isOk());
//    }

    @Test
    void searchVehicle_success() throws Exception {
        // Asume que existe un vehículo con id 1
        mockMvc.perform(get("/api/v1/vehicle/search/1"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateVehicle_success() throws Exception {
        VehicleDto vehicleDto = new VehicleDto(
                null,
                "Toyota",
                "Corolla",
                "Sedan",
                "2021",
                "Negro",
                "Automático",
                "4",
                "Gasolina",
                "urlImagen",
                20000.0,
                true
        );
        String vehicleDtoJson = objectMapper.writeValueAsString(vehicleDto);

        mockMvc.perform(put("/api/v1/vehicle/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void searchVehicle_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/vehicle/search/99"))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateVehicle_notFound() throws Exception {
        VehicleDto vehicleDto = new VehicleDto(
                null,
                "Toyota",
                "Corolla",
                "Sedan",
                "2021",
                "Negro",
                "Automático",
                "4",
                "Gasolina",
                "urlImagen",
                20000.0,
                true
        );
        String vehicleDtoJson = objectMapper.writeValueAsString(vehicleDto);

        mockMvc.perform(put("/api/v1/vehicle/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleDtoJson))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteVehicle_notFound() throws Exception {
        mockMvc.perform(delete("/api/v1/vehicle/delete/99"))
                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
    }
}
