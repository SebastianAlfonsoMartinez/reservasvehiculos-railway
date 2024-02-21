package com.sistemareservas_reservasvehiculos.aplication.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EState;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BookingMapper;
import com.sistemareservas_reservasvehiculos.domain.dto.BookingDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Booking;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.domain.repository.BookingRepository;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingService bookingService;

    private final LocalDateTime sampleDateTime = LocalDateTime.now();
    // Ejemplo de parámetros para VehicleDto
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
    private final BookingDto sampleBookingDto = new BookingDto(1, sampleDateTime, sampleDateTime.plusDays(1)
            , EState.ACTIVE, sampleVehicleDto, sampleUserDto);


    @Test
    void registerBooking() throws BookingException {
        BookingDto bookingDto = new BookingDto(null, sampleDateTime, sampleDateTime.plusDays(1),
                EState.ACTIVE, sampleVehicleDto, sampleUserDto);
        Booking booking = new Booking(null, sampleDateTime, sampleDateTime.plusDays(1),
                EState.ACTIVE, sampleVehicle, null); // inicialmente, el usuario es null

        when(mapper.toEntity(bookingDto)).thenReturn(booking);
        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));

        bookingService.registerBooking(bookingDto, sampleUser.getId());

        assertEquals(sampleUser, booking.getUser());

        verify(bookingRepository, times(1)).save(booking);
    }


    @Test
    void findAllBooking() throws BookingException {
        int offset = 0;
        int limit = 5;
        Booking booking = new Booking(1, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicle, sampleUser);
        List<Booking> bookings = Arrays.asList(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);

        // Parámetros para BookingDto basados en booking
        BookingDto bookingDto = new BookingDto(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getState(),
                sampleVehicleDto,
                sampleUserDto
        );
        List<BookingDto> bookingDtos = Arrays.asList(bookingDto);

        when(bookingRepository.findAll(PageRequest.of(offset, limit))).thenReturn(bookingPage);
        when(mapper.toDtoList(bookings)).thenReturn(bookingDtos);

        List<BookingDto> result = bookingService.findAllBooking(offset, limit);

        assertFalse(result.isEmpty());
        verify(bookingRepository, times(1)).findAll(PageRequest.of(offset, limit));
        verify(mapper, times(1)).toDtoList(bookings);
    }
    @Test
    void findAllBooking_noBookingsFound_throwsBookingException() {
        int offset = 0;
        int limit = 5;

        when(bookingRepository.findAll(PageRequest.of(offset, limit))).thenReturn(new PageImpl<>(Collections.emptyList()));

        assertThrows(BookingException.class, () -> bookingService.findAllBooking(offset, limit));
    }

    @Test
    void findBookingById() throws BookingException {
        int bookingId = 1;
        Booking booking = new Booking(bookingId, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicle, sampleUser);
        BookingDto bookingDto = new BookingDto(bookingId, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicleDto, sampleUserDto);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.findBookingById(bookingId);

        assertEquals(bookingDto, result);
        verify(bookingRepository, times(1)).findById(bookingId);
    }
    @Test
    void findBookingById_bookingNotFound_throwsBookingException() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> bookingService.findBookingById(bookingId));
    }

    @Test
    void updateBooking() throws BookingException {
        int bookingId = 1;
        BookingDto bookingDto = new BookingDto(bookingId, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicleDto, sampleUserDto);
        Booking booking = new Booking(bookingId, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicle, sampleUser);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(mapper.toEntity(bookingDto)).thenReturn(booking);

        bookingService.editBooking(bookingId, bookingDto);

        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).save(booking);
    }
    @Test
    void updateBooking_bookingNotFound_throwsBookingException() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> bookingService.editBooking(bookingId, sampleBookingDto));
    }

    @Test
    void deleteBooking() throws BookingException {
        int bookingId = 1;
        Booking booking = new Booking(bookingId, sampleDateTime, sampleDateTime.plusDays(1), EState.ACTIVE, sampleVehicle, sampleUser);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.removeBooking(bookingId);

        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).delete(booking);
    }
    @Test
    void deleteBooking_bookingNotFound_throwsBookingException() {
        int bookingId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> bookingService.removeBooking(bookingId));
    }
}