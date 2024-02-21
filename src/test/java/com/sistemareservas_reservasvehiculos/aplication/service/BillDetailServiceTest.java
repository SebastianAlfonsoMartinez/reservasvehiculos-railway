package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BillDetailMapper;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDetailDto;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.domain.repository.BillDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillDetailServiceTest {

    @Mock
    private BillDetailRepository billDetailRepository;

    @Mock
    private BillDetailMapper billDetailMapper;

    @InjectMocks
    private BillDetailService billDetailService;

    private final VehicleDto sampleVehicleDto = new VehicleDto(
            1, // id
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
            2, // id
            "Jane", // firstName
            "Doe", // lastName
            "password123", // password
            "jane.doe@example.com", // email
            "1234567890", // phone
            true, // enable
            null,
            Set.of(ERole.USER) // role
    );

    // Ejemplo de parámetros para Vehicle
    private final Vehicle sampleVehicle = new Vehicle(
            1, // id
            "Toyota", // brand
            "Corolla", // reference
            "Sedan", // typeVehicle
            "2021", // manufactureYear
            "Negro", // color
            "Automático", // typeTransmission
            "4", // numberDoors
            "Gasolina", // typeFuel
            "urlImagen", // imageUrl
            20000.0, // price
            true, // available
            null // booking (asumiendo que hay una relación con Booking)
    );

    // Ejemplo de parámetros para User
    private final User sampleUser = new User(
            2, // id
            "Jane", // firstName
            "Doe", // lastName
            "password123", // password
            "jane.doe@example.com", // email
            "1234567890", // phone
            true, // enable
            Set.of(ERole.USER), // role
            null // bookings (asumiendo que hay una relación con Booking)
    );
    private final BillDto sampleBillDto = new BillDto(
            1, // id
            ZonedDateTime.now(), // issuedDate
            250.0, // totalPrice
            sampleVehicleDto, // vehicle
            sampleUserDto // user
    );

    // Ejemplo de parámetros para BillDetailDto
    private final BillDetailDto sampleBillDetailDto = new BillDetailDto(
            1, // id
            "Service Charge", // description
            100.0, // amount
            sampleBillDto // bill
    );

    // Ejemplo de parámetros para Bill
    private final Bill sampleBill = new Bill(
            1, // id
            ZonedDateTime.now(), // issuedDate
            250.0, // totalPrice
            sampleUser, // user
            sampleVehicle, // vehicle
            null // details (asumiendo que es una lista de BillDetail)
    );

    // Ejemplo de parámetros para BillDetail
    private final BillDetail sampleBillDetail = new BillDetail(
            1, // id
            "Service Charge", // description
            100.0, // amount
            sampleBill // bill
    );
    @Test
    void createBillDetail_success() {
        when(billDetailMapper.toEntity(sampleBillDetailDto)).thenReturn(sampleBillDetail);

        billDetailService.createBillDetail(sampleBillDetailDto);

        verify(billDetailRepository, times(1)).save(sampleBillDetail);
    }

    @Test
    void deleteBillDetail_success() throws BookingException {
        when(billDetailRepository.findById(1)).thenReturn(Optional.of(sampleBillDetail));

        billDetailService.deleteBillDetail(1);

        verify(billDetailRepository, times(1)).delete(sampleBillDetail);
    }

    @Test
    void deleteBillDetail_notFound_throwsBookingException() {
        when(billDetailRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billDetailService.deleteBillDetail(1));
    }

    @Test
    void updateBillDetail_success() throws BookingException {
        when(billDetailRepository.findById(1)).thenReturn(Optional.of(sampleBillDetail));
        when(billDetailMapper.toEntity(sampleBillDetailDto)).thenReturn(sampleBillDetail);

        billDetailService.updateBillDetail(1, sampleBillDetailDto);

        verify(billDetailRepository, times(1)).save(sampleBillDetail);
    }

    @Test
    void updateBillDetail_notFound_throwsBookingException() {
        when(billDetailRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billDetailService.updateBillDetail(1, sampleBillDetailDto));
    }

    @Test
    void findAllBillDetail_withDetails() throws BookingException {
        List<BillDetail> details = Collections.singletonList(sampleBillDetail);
        Page<BillDetail> detailPage = new PageImpl<>(details);

        when(billDetailRepository.findAll(PageRequest.of(0, 5))).thenReturn(detailPage);
        when(billDetailMapper.toDtoList(details)).thenReturn(Collections.singletonList(sampleBillDetailDto));

        List<BillDetailDto> result = billDetailService.findAllBillDetail(0, 5);

        assertFalse(result.isEmpty());
        verify(billDetailRepository, times(1)).findAll(PageRequest.of(0, 5));
    }

    @Test
    void findAllBillDetail_noDetails_throwsBookingException() {
        when(billDetailRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());

        assertThrows(BookingException.class, () -> billDetailService.findAllBillDetail(0, 5));
    }

    @Test
    void findBillDetailById_found() throws BookingException {
        when(billDetailRepository.findById(1)).thenReturn(Optional.of(sampleBillDetail));
        when(billDetailMapper.toDto(sampleBillDetail)).thenReturn(sampleBillDetailDto);

        BillDetailDto result = billDetailService.findBillDetailById(1);

        assertEquals(sampleBillDetailDto, result);
        verify(billDetailRepository, times(1)).findById(1);
    }

    @Test
    void findBillDetailById_notFound_throwsBookingException() {
        when(billDetailRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billDetailService.findBillDetailById(1));
    }

}