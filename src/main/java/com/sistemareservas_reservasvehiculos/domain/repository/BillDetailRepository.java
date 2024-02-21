package com.sistemareservas_reservasvehiculos.domain.repository;

import com.sistemareservas_reservasvehiculos.domain.entity.BillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
    Page<BillDetail> findAll(Pageable pageable);
}
