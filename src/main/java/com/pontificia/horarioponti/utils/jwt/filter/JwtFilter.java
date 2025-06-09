package com.pontificia.horarioponti.utils.jwt.filter;

import com.pontificia.horarioponti.utils.jwt.services.CustomUserDetailsService;
import com.pontificia.horarioponti.utils.jwt.services.JwtService;
import com.pontificia.horarioponti.config.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada solicitud HTTP para validar el token JWT.
 * <p>Extiende {@link OncePerRequestFilter} para garantizar una única ejecución por solicitud.</p>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    CustomUserDetailsService userDetailsService;

    /**
     * Intercepta la solicitud HTTP para validar el JWT y configurar la autenticación en el contexto de seguridad.
     *
     * @param request     la solicitud HTTP entrante
     * @param response    la respuesta HTTP saliente
     * @param filterChain el resto de la cadena de filtros
     * @throws ServletException en caso de errores en el procesamiento del filtro
     * @throws IOException      en caso de errores de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extrae el token del encabezado Authorization
            String token = jwtService.parseToken(request);

            // Si el token existe y no hay un usuario ya autenticado en el contexto
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Extrae el nombre de usuario del token
                String username = jwtService.getUsernameFromToken(token);

                if (username != null) {
                    // Carga los detalles del usuario desde la base de datos
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Válida que el token sea válido
                    if (jwtService.isTokenValid(token, userDetails)) {
                        // Genera el token de autenticación y lo configura en el contexto de seguridad
                        UsernamePasswordAuthenticationToken authToken =
                                (UsernamePasswordAuthenticationToken) jwtService.getAuthenticationToken(token, userDetails);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception ex) {
            // Si ocurre un error (token inválido, expirado, etc.), retorna una respuesta 401 con mensaje JSON
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> errorResponse = ApiResponse.error(
                    "Fallo en credenciales",
                    ex.getMessage()
            );

            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
            return;
        }

        // Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

}