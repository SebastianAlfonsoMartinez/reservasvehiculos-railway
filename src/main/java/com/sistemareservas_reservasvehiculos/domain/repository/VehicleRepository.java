package com.sistemareservas_reservasvehiculos.domain.repository;

import com.sistemareservas_reservasvehiculos.domain.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Page<Vehicle> findAll(Pageable pageable);
}
