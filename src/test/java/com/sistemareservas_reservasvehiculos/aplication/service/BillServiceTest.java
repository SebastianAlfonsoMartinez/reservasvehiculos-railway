package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BillMapper;
import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.dto.UserDto;
import com.sistemareservas_reservasvehiculos.domain.dto.VehicleDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import com.sistemareservas_reservasvehiculos.domain.repository.BillRepository;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
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
class BillServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private BillMapper billMapper;

    @InjectMocks
    private BillService billService;

    private final ZonedDateTime sampleDateTime = ZonedDateTime.now();
    private final VehicleDto sampleVehicleDto = new VehicleDto(1, "Toyota", "Corolla"
            , "Sedan", "2021", "Negro", "Automático"
            , "4", "Gasolina", "urlImagen", 20000.0, true);
    private final UserDto sampleUserDto = new UserDto(2, "Jane", "Doe"
            , "password123", "jane.doe@example.com", "1234567890", true, null,
            Set.of(ERole.USER));
    private final Vehicle sampleVehicle = new Vehicle(1, "Toyota", "Corolla"
            , "Sedan", "2021", "Negro", "Automático"
            , "4", "Gasolina", "urlImagen", 20000.0, true, null);
    private final User sampleUser = new User(2, "Jane", "Doe", "password123"
            , "jane.doe@example.com", "1234567890", true,
            Set.of(ERole.USER), null);
    private final BillDto sampleBillDto = new BillDto(1, sampleDateTime, 100.0
            , sampleVehicleDto, sampleUserDto);
    private Bill sampleBill;
    private List<BillDetail> sampleBillDetails;

    @BeforeEach
    void setUp() {
        // Inicializa sampleBill y luego sampleBillDetails para evitar la referencia circular
        sampleBill = new Bill(
                1, // id
                ZonedDateTime.now(), // issuedDate
                250.0, // totalPrice
                sampleUser, // user
                sampleVehicle, // vehicle
                null // details (se establecerán después)
        );

        sampleBillDetails = Collections.singletonList(
                new BillDetail(1, "Service Charge", 100.0, sampleBill)
        );

        // Ahora que sampleBillDetails está inicializado, actualiza sampleBill con estos detalles
        sampleBill.setDetails(sampleBillDetails);
    }

    @Test
    void createBill_success() throws BookingException {
        // Preparación de la prueba
        Bill bill = new Bill(1, sampleDateTime, 100.0, sampleUser, sampleVehicle, sampleBillDetails);
        when(billMapper.toEntity(sampleBillDto)).thenReturn(bill);
        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));

        // Ejecutar el método bajo prueba
        billService.createBill(sampleBillDto, sampleUser.getId());

        // Verificar comportamientos esperados
        verify(billRepository, times(1)).save(bill);
    }


    @Test
    void findAllBill_withBills() throws BookingException {
        List<Bill> bills = Collections.singletonList(new Bill(1, sampleDateTime, 100.0, sampleUser
                , sampleVehicle, sampleBillDetails));
        Page<Bill> billPage = new PageImpl<>(bills);

        when(billRepository.findAll(PageRequest.of(0, 5))).thenReturn(billPage);
        when(billMapper.toDtoList(bills)).thenReturn(Collections.singletonList(sampleBillDto));

        List<BillDto> result = billService.findAllBill(0, 5);

        assertFalse(result.isEmpty());
        verify(billRepository, times(1)).findAll(PageRequest.of(0, 5));
    }
    @Test
    void findBillById_found() throws BookingException {
        Bill bill = new Bill(1, sampleDateTime, 100.0, sampleUser, sampleVehicle, sampleBillDetails);

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));
        when(billMapper.toDto(bill)).thenReturn(sampleBillDto);

        BillDto result = billService.findBillById(1);

        assertEquals(sampleBillDto, result);
        verify(billRepository, times(1)).findById(1);
    }

    @Test
    void findBillById_notFound_throwsBookingException() {
        when(billRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billService.findBillById(1));
    }

    @Test
    void deleteBill_success() throws BookingException {
        Bill bill = new Bill(1, sampleDateTime, 100.0, sampleUser, sampleVehicle, sampleBillDetails);

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));

        billService.deleteBill(1);

        verify(billRepository, times(1)).delete(bill);
    }

    @Test
    void deleteBill_notFound_throwsBookingException() {
        when(billRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billService.deleteBill(1));
    }

    @Test
    void updateBill_success() throws BookingException {
        BillDto billDto = new BillDto(1, sampleDateTime, 100.0, sampleVehicleDto, sampleUserDto);
        Bill bill = new Bill(1, sampleDateTime, 100.0, sampleUser, sampleVehicle, sampleBillDetails);

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));
        when(billMapper.toEntity(billDto)).thenReturn(bill);

        billService.updateBill(1, billDto);

        verify(billRepository, times(1)).save(bill);
    }

    @Test
    void updateBill_notFound_throwsBookingException() {
        BillDto billDto = new BillDto(1, sampleDateTime, 100.0, sampleVehicleDto, sampleUserDto);

        when(billRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> billService.updateBill(1, billDto));
    }

}