package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.mapper.VehicleMapper;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleMapper mapper;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createVehicle() {
        // Crear un VehicleDto con los datos de prueba
        VehicleDto vehicleDto = new VehicleDto(
                null, // El ID generalmente es nulo para una creación
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

        // Crear una entidad Vehicle que será el resultado del mapeo
        Vehicle vehicle = new Vehicle(
                null, // El ID generalmente es nulo para una nueva entidad
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
                true,
                null // Suponiendo que hay una relación con otra entidad, como Booking
        );

        // Configurar el comportamiento del mapper
        when(mapper.toEntity(vehicleDto)).thenReturn(vehicle);

        // Ejecutar el método a probar
        vehicleService.createVehicle(vehicleDto);

        // Verificar que el mapper y el repositorio son llamados con los argumentos correctos
        verify(mapper, times(1)).toEntity(vehicleDto);
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void vehicleList() throws BookingException {
        // Datos de prueba
        int offset = 0;
        int limit = 5;

        // Crear lista de objetos Vehicle para la prueba
        Vehicle vehicle1 = new Vehicle(1, "Toyota", "Corolla", "Sedan"
                , "2021", "Negro", "Automático", "4"
                , "Gasolina", "urlImagen1", 20000.0, true, null);
        Vehicle vehicle2 = new Vehicle(2, "Honda", "Civic", "Hatchback"
                , "2020", "Blanco", "Manual", "4"
                , "Gasolina", "urlImagen2", 18000.0, true, null);
        List<Vehicle> vehicleList = Arrays.asList(vehicle1, vehicle2);
        Page<Vehicle> vehiclePage = new PageImpl<>(vehicleList);

        // Crear lista de objetos VehicleDto para la prueba
        VehicleDto vehicleDto1 = new VehicleDto(1, "Toyota", "Corolla", "Sedan"
                , "2021", "Negro", "Automático", "4"
                , "Gasolina", "urlImagen1", 20000.0, true);
        VehicleDto vehicleDto2 = new VehicleDto(2, "Honda", "Civic", "Hatchback"
                , "2020", "Blanco", "Manual", "4"
                , "Gasolina", "urlImagen2", 18000.0, true);
        List<VehicleDto> vehicleDtoList = Arrays.asList(vehicleDto1, vehicleDto2);

        // Configurar el comportamiento del repository y del mapper
        when(vehicleRepository.findAll(PageRequest.of(offset, limit))).thenReturn(vehiclePage);
        when(mapper.toDtoList(vehicleList)).thenReturn(vehicleDtoList);

        // Ejecutar el método a probar
        List<VehicleDto> result = vehicleService.vehicleList(offset, limit);

        // Verificaciones
        assertEquals(vehicleDtoList.size(), result.size(), "La lista devuelta debe tener el mismo tamaño que la lista de DTOs");
        verify(vehicleRepository, times(1)).findAll(PageRequest.of(offset, limit));
        verify(mapper, times(1)).toDtoList(vehicleList);
    }

    @Test
    void vehicleListEmpty() {
        // Caso de prueba cuando no hay vehículos
        int offset = 0;
        int limit = 5;
        Page<Vehicle> emptyPage = new PageImpl<>(Arrays.asList());

        when(vehicleRepository.findAll(PageRequest.of(offset, limit))).thenReturn(emptyPage);

        // Verificar que se lanza la excepción BookingException
        assertThrows(BookingException.class, () -> {
            vehicleService.vehicleList(offset, limit);
        });
    }
    @Test
    void findVehicleById() throws BookingException {
        // ID de prueba y datos del vehículo
        Integer vehicleId = 1;
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", "Sedan", "2021", "Negro", "Automático", "4", "Gasolina", "urlImagen", 20000.0, true, null);
        VehicleDto vehicleDto = new VehicleDto(1, "Toyota", "Corolla", "Sedan", "2021", "Negro", "Automático", "4", "Gasolina", "urlImagen", 20000.0, true);

        // Configurar el comportamiento del repository y del mapper
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(mapper.toDto(vehicle)).thenReturn(vehicleDto);

        // Ejecutar el método a probar
        VehicleDto result = vehicleService.findVehicleById(vehicleId);

        // Verificaciones
        assertEquals(vehicleDto, result, "El VehicleDto devuelto debe coincidir con el esperado");
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(mapper, times(1)).toDto(vehicle);
    }
    @Test
    void findVehicleByIdNotFound() {
        // ID de prueba para un vehículo que no existe
        Integer vehicleId = 99;

        // Configurar el comportamiento del repository para simular que no se encuentra el vehículo
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción BookingException
        assertThrows(BookingException.class, () -> {
            vehicleService.findVehicleById(vehicleId);
        });
    }
    @Test
    void deleteVehicle() throws BookingException {
        // ID de prueba y datos del vehículo
        Integer vehicleId = 1;
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", "Sedan", "2021", "Negro", "Automático", "4", "Gasolina", "urlImagen", 20000.0, true, null);

        // Configurar el comportamiento del repository
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Ejecutar el método a probar
        vehicleService.deleteVehicle(vehicleId);

        // Verificaciones
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(vehicleRepository, times(1)).delete(vehicle);
    }
    @Test
    void deleteVehicleNotFound() {
        // ID de prueba para un vehículo que no existe
        Integer vehicleId = 99;

        // Configurar el comportamiento del repository para simular que no se encuentra el vehículo
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción BookingException
        assertThrows(BookingException.class, () -> {
            vehicleService.deleteVehicle(vehicleId);
        });
    }
    @Test
    void updateVehicle() throws BookingException {
        // ID de prueba y datos del DTO y entidad
        Integer vehicleId = 1;
        VehicleDto vehicleDto = new VehicleDto(1, "Toyota", "Corolla", "Sedan", "2022", "Azul", "Automático", "4", "Gasolina", "urlImagenNueva", 21000.0, true);
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", "Sedan", "2021", "Negro", "Automático", "4", "Gasolina", "urlImagen", 20000.0, true, null);

        // Configurar el comportamiento del repository y del mapper
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(mapper.toEntity(vehicleDto)).thenReturn(vehicle);

        // Ejecutar el método a probar
        vehicleService.updateVehicle(vehicleId, vehicleDto);

        // Verificaciones
        verify(vehicleRepository, times(1)).findById(vehicleId);
        verify(mapper, times(1)).toEntity(vehicleDto);
        verify(vehicleRepository, times(1)).save(vehicle);
    }
    @Test
    void updateVehicleNotFound() {
        // ID de prueba para un vehículo que no existe
        Integer vehicleId = 99;
        VehicleDto vehicleDto = new VehicleDto(99, "Honda", "Civic", "Coupe", "2023", "Rojo", "Manual", "2", "Gasolina", "urlImagenHonda", 22000.0, true);

        // Configurar el comportamiento del repository para simular que no se encuentra el vehículo
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción BookingException
        assertThrows(BookingException.class, () -> {
            vehicleService.updateVehicle(vehicleId, vehicleDto);
        });
    }
}








