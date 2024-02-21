package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDto;
import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BillMapper;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.repository.BillRepository;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public record BillService (
        BillRepository billRepository,
        UserRepository userRepository,
        BillMapper mapper
){
    public void  createBill(BillDto billDto, int userId) throws BookingException {
        Bill bill = mapper.toEntity(billDto);
        User user = userRepository.findById(userId).orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        bill.setUser(user);
        billRepository.save(bill);
    }

    public List<BillDto> findAllBill(Integer offset, Integer limit) throws BookingException{
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Bill> bills = billRepository.findAll(pageable);
        if(bills.getContent().isEmpty())
            throw new BookingException(EMessage.DATA_NOT_FOUND);
        return mapper.toDtoList(bills.getContent());
    }

    public BillDto findBillById(Integer id)throws BookingException{
        Bill bill = billRepository.findById(id).
                orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        return mapper.toDto(bill);
    }

    public void deleteBill(Integer id)throws BookingException{
        Bill bill = billRepository.findById(id)
                .orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        billRepository.delete(bill);
    }
    public void updateBill(Integer id, BillDto billDto) throws BookingException{
        billRepository.findById(id)
                .orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        Bill bill = mapper.toEntity(billDto);
        billRepository.save(bill);
    }

}
