package com.sistemareservas_reservasvehiculos.aplication.service;

import com.sistemareservas_reservasvehiculos.domain.dto.BillDetailDto;
import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
import com.sistemareservas_reservasvehiculos.aplication.exception.BookingException;
import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.aplication.mapper.BillDetailMapper;
import com.sistemareservas_reservasvehiculos.domain.repository.BillDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BillDetailService(
        BillDetailRepository billDetailRepository,
        BillDetailMapper mapper
) {

    public void createBillDetail(BillDetailDto billDetailDto){
        BillDetail billDetail = mapper.toEntity(billDetailDto);
        billDetailRepository.save(billDetail);
    }

    public void deleteBillDetail(Integer id) throws BookingException {
        BillDetail billDetail = billDetailRepository.findById(id)
                .orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        billDetailRepository.delete(billDetail);
    }
    public void updateBillDetail(Integer id, BillDetailDto billDetailDto) throws BookingException {
        billDetailRepository.findById(id)
                .orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        BillDetail billDetail = mapper.toEntity(billDetailDto);
        billDetailRepository.save(billDetail);
    }

    public List<BillDetailDto> findAllBillDetail(Integer offset, Integer limit) throws BookingException {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<BillDetail> detailPage = billDetailRepository.findAll(pageable);
        if (detailPage.getContent().isEmpty())
            throw new BookingException(EMessage.DATA_NOT_FOUND);
        return mapper.toDtoList(detailPage.getContent());
    }
    public BillDetailDto findBillDetailById(Integer id) throws BookingException{
        BillDetail billDetail = billDetailRepository.findById(id)
                .orElseThrow(()-> new BookingException(EMessage.DATA_NOT_FOUND));
        return mapper.toDto(billDetail);
    }

}
