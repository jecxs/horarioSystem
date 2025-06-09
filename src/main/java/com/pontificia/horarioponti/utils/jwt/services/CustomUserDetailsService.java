package com.pontificia.horarioponti.utils.jwt.services;

import com.pontificia.horarioponti.modules.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado que implementa {@link UserDetailsService} para cargar
 * detalles del usuario desde la base de datos.
 * <p>Este servicio es utilizado por Spring Security durante el proceso de autenticación.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userRepository repositorio de usuarios
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga el usuario desde la base de datos usando el nombre de usuario (username).
     *
     * @param username el nombre de usuario a buscar
     * @return una instancia de {@link UserDetails}
     * @throws UsernameNotFoundException si el usuario no se encuentra
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return (UserDetails) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
