package com.sistemareservas_reservasvehiculos.aplication.config;

import com.sistemareservas_reservasvehiculos.aplication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  // Inyecta un servicio de detalles de usuario para cargar información de usuario
  private final UserDetailsService userDetailsService;
  // Inyecta un servicio para manejar operaciones relacionadas con JWT
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    // Intenta obtener el token JWT del encabezado 'Authorization' de la solicitud HTTP
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    // Verifica si el encabezado de autorización es nulo o no empieza con "Bearer "
    // Si no cumple, continúa con el resto de los filtros sin hacer nada más
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Extrae el token JWT del encabezado de autorización (omitindo "Bearer ")
    jwt = authHeader.substring(7);
    // Usa el servicio JWT para extraer el nombre de usuario (email) del token
    userEmail = jwtService.extractUserName(jwt);

    // Si se extrajo un email y no existe aún una autenticación para esta solicitud,
    // procede a autenticar al usuario en el contexto de seguridad
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Carga los detalles del usuario mediante el servicio de detalles de usuario
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      // Si el token es válido, establece la autenticación del usuario en el contexto de seguridad
      setAuthenticationToContext(request, jwt, userDetails);
    }
    // Continúa con la cadena de filtros
    filterChain.doFilter(request, response);
  }

  // Establece la autenticación del usuario en el contexto de seguridad de Spring si el token es válido
  private void setAuthenticationToContext(HttpServletRequest request, String jwt, UserDetails userDetails) {
    if (jwtService.isTokenValid(jwt, userDetails)) {
      // Crea un token de autenticación con los detalles del usuario y sus autoridades
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
      );
      // Establece los detalles de la autenticación basados en la solicitud HTTP
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      // Actualiza el contexto de seguridad con el token de autenticación del usuario
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
  }
}
