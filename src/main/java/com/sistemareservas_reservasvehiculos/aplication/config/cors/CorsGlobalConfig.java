package com.sistemareservas_reservasvehiculos.aplication.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    // Define un bean que configura CORS a nivel global para la aplicación
    @Bean
    public WebMvcConfigurer corsConfig() {
        // Retorna una instancia anónima de WebMvcConfigurer con configuración personalizada de CORS
        return new WebMvcConfigurer() {

            // Sobrescribe el método addCorsMappings para configurar las políticas de CORS
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Agrega configuración de CORS para todas las rutas (/**)
                registry.addMapping("/**")
                        .allowedOrigins("https://reservasvehiculos-railway-production.up.railway.app/") // Permite solicitudes de cualquier origen. En producción, reemplaza "*" con las URLs específicas de tus frontends para mayor seguridad
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Especifica los métodos HTTP permitidos para las solicitudes CORS
            }
        };
    }
}