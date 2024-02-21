package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.domain.dto.BookingDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Booking;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EState;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BookingMapper;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.repository.BookingRepository;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BookingService (
        BookingRepository bookingRepository,
        UserRepository userRepository,
        BookingMapper mapper
){
    // Registra una nueva reserva asociándola a un usuario y la marca como activa.
    public void registerBooking(BookingDto bookingDto, int userId) throws BookingException {
        Booking booking = mapper.toEntity(bookingDto); // Convierte DTO a entidad
        booking.setState(EState.ACTIVE); // Establece el estado de la reserva a activo
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingException(EMessage.USER_NOT_FOUND)); // Busca el usuario o lanza excepción si no se encuentra
        booking.setUser(user); // Asocia el usuario a la reserva
        bookingRepository.save(booking); // Guarda la reserva en la base de datos
    }

    // Encuentra todas las reservas aplicando paginación y las devuelve como una lista de DTOs.
    public List<BookingDto> findAllBooking(Integer offset, Integer limit) throws BookingException {
        Pageable pageable = PageRequest.of(offset, limit); // Crea objeto de paginación
        Page<Booking> bookings = bookingRepository.findAll(pageable); // Encuentra las reservas con paginación
        if (bookings.getContent().isEmpty()) {
            throw new BookingException(EMessage.DATA_NOT_FOUND); // Lanza excepción si no se encuentran datos
        }
        return mapper.toDtoList(bookings.getContent()); // Convierte las entidades encontradas a DTOs
    }

    // Busca una reserva por su ID y la devuelve como DTO.
    public BookingDto findBookingById(Integer id) throws BookingException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND)); // Busca la reserva o lanza excepción si no se encuentra
        return mapper.toDto(booking); // Convierte la entidad a DTO
    }

    // Edita una reserva existente basada en los datos proporcionados en el DTO.
    public void editBooking(Integer id, BookingDto bookingDto) throws BookingException {
        bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND)); // Verifica que la reserva exista
        Booking booking = mapper.toEntity(bookingDto); // Convierte DTO a entidad
        bookingRepository.save(booking); // Guarda la reserva actualizada en la base de datos
    }

    // Elimina una reserva por su ID.
    public void removeBooking(Integer id) throws BookingException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND)); // Verifica que la reserva exista
        bookingRepository.delete(booking); // Elimina la reserva de la base de datos
    }
}
