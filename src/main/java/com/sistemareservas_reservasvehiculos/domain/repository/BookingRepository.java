package com.sistemareservas_reservasvehiculos.domain.repository;

import com.sistemareservas_reservasvehiculos.domain.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAll(Pageable pageable);
}
