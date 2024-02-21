package com.sistemareservas_reservasvehiculos.domain.repository;

import com.sistemareservas_reservasvehiculos.domain.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    Page<Bill> findAll(Pageable pageable);
}
