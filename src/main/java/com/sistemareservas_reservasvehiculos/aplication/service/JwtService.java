package com.sistemareservas_reservasvehiculos.aplication.service;


import com.sistemareservas_reservasvehiculos.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public record JwtService(
        // Inyecta la clave secreta y el tiempo de expiración del JWT desde el archivo de propiedades de la aplicación
        @Value("${application.security.jwt.secret-key}")
        String secretKey,

        @Value("${application.security.jwt.expiration}")
        Long jwtExpiration) {

  // Extrae el nombre de usuario del token JWT utilizando el claim 'subject'
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Método genérico para extraer un claim específico del token JWT
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Genera un token JWT para un usuario, sin claims adicionales
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  // Genera un token JWT incluyendo claims adicionales proporcionados y los detalles del usuario
  private String generateToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {
    // Agrega información adicional del usuario, como roles y nombre completo, a los claims
    if (userDetails instanceof User) {
      extraClaims.put("roles", ((User) userDetails).getRoles());
      extraClaims.put("fullName", ((User) userDetails).getFirstName() + " " + ((User) userDetails).getLastName());
      extraClaims.put("id_user", ((User) userDetails).getId());
    }
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  // Construye el token JWT con los claims dados, usuario y tiempo de expiración
  private String buildToken(HashMap<String, Object> extraClaims, UserDetails userDetails, Long expiration) {
    JwtBuilder builder = Jwts.builder()
            .setSubject(userDetails.getUsername()) // Establece el nombre de usuario como subject del token
            .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())) // Fecha de emisión
            .setExpiration(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().plusMillis(expiration))) // Fecha de expiración
            .signWith(getSignInKey(), SignatureAlgorithm.HS256); // Firma el token con la clave secreta y algoritmo HS256

    // Agrega los claims adicionales al token, excepto los roles
    extraClaims.forEach((key, value) -> {
      if (!key.equals("role")) {
        builder.claim(key, value);
      }
    });

    // Si se incluyen roles, los agrega específicamente
    if (extraClaims.containsKey("role")) {
      builder.claim("role", extraClaims.get("role"));
    }

    return builder.compact(); // Construye el token JWT y lo devuelve como String
  }

  // Extrae todos los claims de un token JWT dado
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignInKey()) // Configura la clave de firma para la validación del token
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  // Obtiene la clave de firma para firmar o validar tokens, decodificando la clave secreta configurada
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // Valida un token JWT comparando el nombre de usuario del token y el usuario proporcionado y verificando que no haya expirado
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  // Verifica si el token JWT ha expirado comparando la fecha de expiración del token con la fecha y hora actual
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).isBefore(LocalDateTime.now());
  }

  // Extrae la fecha de expiración del token JWT y la convierte a LocalDateTime
  private LocalDateTime extractExpiration(String token) {
    Date date = extractClaim(token, Claims::getExpiration);
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
