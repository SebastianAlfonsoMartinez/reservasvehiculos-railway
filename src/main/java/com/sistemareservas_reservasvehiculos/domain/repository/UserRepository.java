package com.sistemareservas_reservasvehiculos.domain.repository;

import com.sistemareservas_reservasvehiculos.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
