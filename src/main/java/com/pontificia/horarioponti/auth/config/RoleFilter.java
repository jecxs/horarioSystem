package com.pontificia.horarioponti.auth.config;

import com.pontificia.horarioponti.repository.model.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RoleFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Excluir rutas públicas del filtro
        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtils.parseToken(request);

        if (token == null || !jwtUtils.validateToken(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token inválido o faltante");
            return;
        }

        Role requiredRole = getRequiredRoleForPath(request.getRequestURI());
        if (requiredRole == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String roleString = jwtUtils.getRoleFromToken(token);
        Role userRole = Role.valueOf(roleString);

        if (!hasRequiredRole(userRole, requiredRole)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "No tienes permisos para acceder a este recurso");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth") ||
                path.startsWith("/swagger") ||
                path.startsWith("/v3/api-docs");
    }

    private boolean hasRequiredRole(Role userRole, Role requiredRole) {
        // Jerarquía de roles (SUPER_ADMIN > ADMIN > TEACHER > SECRETARIAT)
        return userRole.ordinal() <= requiredRole.ordinal();
    }

    private Role getRequiredRoleForPath(String path) {
        if (path.startsWith("/api/super-admin")) {
            return Role.SUPER_ADMIN;
        }
        if (path.startsWith("/api/admin")) {
            return Role.ADMIN;
        }
        if (path.startsWith("/api/teacher")) {
            return Role.TEACHER;
        }
        if (path.startsWith("/api/secretariat")) {
            return Role.SECRETARIAT;
        }
        return null; // Rutas sin requerimiento específico
    }

}
