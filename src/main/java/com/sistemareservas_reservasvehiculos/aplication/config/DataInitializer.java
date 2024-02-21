package com.sistemareservas_reservasvehiculos.aplication.config;

import com.sistemareservas_reservasvehiculos.aplication.lasting.ERole;
import com.sistemareservas_reservasvehiculos.domain.entity.User;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        // Verifica si ya existe un usuario administrador
        Optional<User> adminOptional = userRepository.findUserByEmail("admin@admin.com");

        if (adminOptional.isEmpty()) {
            // Si no existe, crea uno
            User admin = new User();
            admin.setEmail("admin@admin.com"); // Utiliza un correo electrónico para el administrador
            admin.setPassword(passwordEncoder.encode("Admin#2024*"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEnable(true);
            admin.setPhone("123456789");
            // Establece el rol de ADMIN
            admin.setRoles(Set.of(ERole.ADMIN));

            userRepository.save(admin);
        }
    }
}
