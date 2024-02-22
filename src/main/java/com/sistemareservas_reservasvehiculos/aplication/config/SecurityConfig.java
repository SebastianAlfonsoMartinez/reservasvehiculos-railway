package com.sistemareservas_reservasvehiculos.aplication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    // Inyección del filtro JWT personalizado para manejar la validación de tokens JWT en las peticiones
    private final JwtFilter jwtFilter;
    // Proveedor de autenticación personalizado para integrar con la lógica de autenticación existente
    private final AuthenticationProvider authenticationProvider;

    private  final CorsFilter corsFilter;

    // Lista de URLs que no requieren autenticación. Todas las demás solicitudes necesitarán estar autenticadas.
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**", // Rutas de autenticación
            "/swagger-ui/**", // Documentación de la API con Swagger
            "/v3/api-docs/**", // Endpoints de documentación de Swagger
            "/api/v1/vehicle/all/{offset}/{limit}", // Endpoint público para listar vehículos
            "/api/v1/vehicle/search/**" // Endpoint público para buscar vehículos
    };

    // Define la cadena de filtros de seguridad principal para la aplicación
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .csrf(AbstractHttpConfigurer::disable) // Desactiva la protección CSRF, común en APIs REST
                .authorizeHttpRequests( // Configura las reglas de autorización
                        req -> req
                                .requestMatchers(WHITE_LIST_URL) // Aplica las reglas a las URLs en la lista blanca
                                .permitAll() // Permite el acceso sin autenticación a las URLs en la lista blanca
                                .anyRequest() // Todas las demás solicitudes
                                .authenticated() // Deben estar autenticadas
                )
                .sessionManagement( // Configura la gestión de sesiones para ser sin estado
                        sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Establece el proveedor de autenticación
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro JWT antes del filtro de autenticación de usuario y contraseña
        return http.build(); // Construye y devuelve la configuración de seguridad
    }
}
