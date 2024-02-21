package com.sistemareservas_reservasvehiculos.aplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingExceptionHandler;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;
    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(userController, authenticationController)
                .setControllerAdvice(new BookingExceptionHandler())
                .build();
    }

    @Test
    void createUser_success() throws Exception {
        String uniqueEmail = "janedoe" + System.currentTimeMillis() + "@example.com";
        // Asume que roles y reservas son manejados apropiadamente en tus pruebas o cuando creas UserDto
        UserDto userDto = new UserDto(
                1, // ID es null para un nuevo usuario
                "Jane", // firstName
                "Doe", // lastName
                "password123", // password
                uniqueEmail, // email
                "1234567890", // phone
                true, // enable
                null, // bookings, asumido null o manejado específicamente si es necesario
                Set.of(ERole.USER) // roles
        );
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isCreated());
    }


//    @Test
//    @Transactional
//    @WithMockUser(roles = {"ADMIN"})
//    void searchAll_success() throws Exception {
//        mockMvc.perform(get("/api/v1/user/all/0/10"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(roles = {"ADMIN"})
//    void searchUser_success() throws Exception {
//        // Asume que existe un usuario con id 1
//        mockMvc.perform(get("/api/v1/user/search/1"))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    @WithMockUser(roles = {"ADMIN"})
//    void updateUser_success() throws Exception {
//        UserDto userDto = new UserDto(
//                1, // id
//                "Jane", // firstName
//                "Doe", // lastName
//                "password123", // password
//                "jane.doe@example.com", // email
//                "1234567890", // phone
//                true, // enable
//                null, // bookings, aquí se pasa null ya que no es relevante para la prueba
//                Set.of(ERole.USER) // roles
//        );
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(put("/api/v1/user/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userDtoJson))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void searchUser_notFound() throws Exception {
//        mockMvc.perform(get("/api/v1/user/search/99"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
//    }
//
//    @Test
//    void updateUser_notFound() throws Exception {
//        UserDto userDto = new UserDto(
//                1, "Jane", "Doe", "password123", "jane.doe@example.com", "1234567890", true, ERole.USER
//        );
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(put("/api/v1/user/update/99")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userDtoJson))
//                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
//                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
//    }
//    @Test
//    void deleteUser_notFound() throws Exception {
//        mockMvc.perform(delete("/api/v1/user/delete/99"))
//                .andExpect(status().is(EMessage.DATA_NOT_FOUND.getStatus().value()))
//                .andExpect(jsonPath("$.message").value(EMessage.DATA_NOT_FOUND.getMessage()));
//    }
}