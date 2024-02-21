package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.mapper.VehicleMapper;
import com.sistemareservas_reservasvehiculos.domain.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record VehicleService(
        VehicleRepository vehicleRepository,
        VehicleMapper mapper) {

    public void createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = mapper.toEntity(vehicleDto);
        vehicleRepository.save(vehicle);

    }

    public List<VehicleDto> vehicleList(Integer offset, Integer limit) throws BookingException {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        if (vehicles.getContent().isEmpty()){
            throw new BookingException(EMessage.DATA_NOT_FOUND);
        }
        return mapper.toDtoList(vehicles.getContent());
    }

    public VehicleDto findVehicleById(Integer id)throws BookingException {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                new BookingException(EMessage.DATA_NOT_FOUND));
        return mapper.toDto(vehicle);

    }

    public void deleteVehicle(Integer id) throws BookingException {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                new BookingException(EMessage.DATA_NOT_FOUND));
        vehicleRepository.delete(vehicle);
    }

    public void updateVehicle(Integer id, VehicleDto vehicleDto) throws BookingException {
        vehicleRepository.findById(id).orElseThrow(() -> new BookingException(EMessage.DATA_NOT_FOUND));
        Vehicle vehicle = mapper.toEntity(vehicleDto);
        vehicleRepository.save(vehicle);
    }
}
