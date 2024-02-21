package com.sistemareservas_reservasvehiculos.aplication.config;

import com.sistemareservas_reservasvehiculos.aplication.lasting.EMessage;
import com.sistemareservas_reservasvehiculos.domain.repository.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // Inyecta el repositorio de usuarios para ser utilizado en el servicio de detalles de usuario
    private final UserRepository userRepository;

    // Define un bean para el servicio de detalles de usuario de Spring Security
    @Bean
    public UserDetailsService userDetailsService() {
        // La implementación busca un usuario por email y lanza una excepción si no lo encuentra
        return username -> userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(EMessage.USER_NOT_FOUND.getMessage()));
    }

    // Configura el proveedor de autenticación con el servicio de detalles de usuario y codificador de contraseñas
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Establece el servicio de detalles de usuario
        authenticationProvider.setUserDetailsService(userDetailsService());
        // Establece el codificador de contraseñas para verificar las contraseñas de los usuarios
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Define un bean para el codificador de contraseñas, utilizando BCrypt como algoritmo de hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura el gestor de autenticación de Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Obtiene el AuthenticationManager configurado por Spring Security
        return configuration.getAuthenticationManager();
    }

    // Configura la seguridad de la API especificando el esquema de autenticación JWT para la documentación de OpenAPI
    @Bean
    public OpenAPI customOpenAPI() {
        // Crea y configura un objeto OpenAPI para documentar la autenticación basada en JWT
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", // Define un esquema de seguridad llamado "bearerAuth"
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP) // Tipo HTTP
                                        .scheme("bearer") // Esquema Bearer
                                        .bearerFormat("JWT") // Formato del portador: JWT
                        )
                );
    }

}
