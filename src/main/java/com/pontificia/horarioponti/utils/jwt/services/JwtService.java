package com.pontificia.horarioponti.utils.jwt.services;

import com.pontificia.horarioponti.enums.ERole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * Servicio que gestiona la generación, validación y análisis de tokens JWT.
 */
@Service
public class JwtService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;

    private static final ERole DEFAULT_ROLE = ERole.TEACHER;

    /**
     * Obtiene la clave de firma HMAC a partir del secreto JWT configurado.
     *
     * @return clave de firma {@link Key}
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT con los datos del usuario.
     *
     * @param userId   UUID del usuario
     * @param username nombre de usuario
     * @param roleStr  rol del usuario en formato String
     * @return token JWT generado
     */
    public String generateToken(UUID userId, String username, String roleStr) {
        ERole userRole;
        try {
            userRole = (roleStr != null && !roleStr.isEmpty())
                    ? ERole.valueOf(roleStr)
                    : DEFAULT_ROLE;
        } catch (IllegalArgumentException e) {
            userRole = DEFAULT_ROLE;
        }

        return Jwts.builder()
                .claim("userId", userId.toString())
                .claim("username", username)
                .claim("role", userRole.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario del token.
     *
     * @param token el JWT
     * @return el nombre de usuario contenido en el token
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).get("username", String.class);
    }

    /**
     * Extrae todos los claims (atributos) del token JWT.
     *
     * @param token el JWT
     * @return claims contenidos en el token
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si el token es válido para el usuario proporcionado.
     *
     * @param token        el JWT
     * @param userDetails  detalles del usuario autenticado
     * @return true si es válido y no ha expirado, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            String username = getUsernameFromToken(token);

            boolean isUsernameValid = username != null && username.equals(userDetails.getUsername());

            boolean isExpired = isTokenExpired(token);

            return isUsernameValid && !isExpired;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Error al verificar el token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token el JWT
     * @return true si está expirado, false si aún es válido
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Construye el objeto {@link Authentication} a partir del token y detalles del usuario.
     *
     * @param token        el JWT
     * @param userDetails  detalles del usuario
     * @return objeto de autenticación
     */
    public Authentication getAuthenticationToken(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * Extrae el token JWT del encabezado Authorization de la solicitud.
     *
     * @param request objeto {@link HttpServletRequest}
     * @return el token JWT sin el prefijo "Bearer ", o null si no existe
     */
    public String parseToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
