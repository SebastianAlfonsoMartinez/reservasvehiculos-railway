package com.sistemareservas_reservasvehiculos.aplication.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    // Define un bean que configura CORS a nivel global para la aplicación

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("https://reservasvehiculos-railway-production.up.railway.app/");
        configuration.addAllowedOrigin("https://sebastianalfonsomartinez.github.io/");
        configuration.addAllowedOrigin("http://localhost:4200/");
        configuration.addAllowedOrigin("http://localhost:8181/");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
//    @Bean
//    public WebMvcConfigurer corsConfig() {
//        // Retorna una instancia anónima de WebMvcConfigurer con configuración personalizada de CORS
//        return new WebMvcConfigurer() {
//
//            // Sobrescribe el método addCorsMappings para configurar las políticas de CORS
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                // Agrega configuración de CORS para todas las rutas (/**)
//                registry.addMapping("/**")
//                        .allowedOrigins("https://reservasvehiculos-railway-production.up.railway.app/") // Permite solicitudes de cualquier origen. En producción, reemplaza "*" con las URLs específicas de tus frontends para mayor seguridad
//                        .allowedOrigins("https://sebastianalfonsomartinez.github.io/") // Permite solicitudes de cualquier origen. En producción, reemplaza "*" con las URLs específicas de tus frontends para mayor seguridad
//                        .allowedOrigins("http://localhost:4200/") // Permite solicitudes de cualquier origen. En producción, reemplaza "*" con las URLs específicas de tus frontends para mayor seguridad
//                        .allowedOrigins("http://localhost:8181/:") // Permite solicitudes de cualquier origen. En producción, reemplaza "*" con las URLs específicas de tus frontends para mayor seguridad
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Especifica los métodos HTTP permitidos para las solicitudes CORS
//            }
        };
//    }
//}