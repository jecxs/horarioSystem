package com.pontificia.horarioponti.config;

import com.pontificia.horarioponti.utils.jwt.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuración de seguridad para la aplicación.
 * Define políticas de seguridad HTTP, CORS, autenticación y gestión de sesiones.
 * Implementa WebMvcConfigurer para configuración personalizada de CORS.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * Configura las reglas CORS para permitir solicitudes desde orígenes específicos.
     * En este caso, permite solicitudes desde "allowedOrigins" con ciertos métodos y cabeceras.
     *
     * @param registry Registry para agregar reglas CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }

    /**
     * Define la cadena de filtros de seguridad HTTP.
     * Configura CSRF deshabilitado, reglas de autorización, gestión sin estado de sesión
     * y agrega el filtro JWT para validar tokens antes del filtro de autenticación estándar.
     *
     * @param http Configuración HTTP para personalizar la seguridad.
     * @return SecurityFilterChain configurado.
     * @throws Exception en caso de error al construir la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provee el AuthenticationManager utilizado por Spring Security.
     * Es necesario para la autenticación manual en servicios personalizados.
     *
     * @param config Configuración de autenticación.
     * @return AuthenticationManager para gestionar autenticaciones.
     * @throws Exception en caso de error.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el PasswordEncoder para el hashing de contraseñas.
     * En este caso se usa BCrypt, un algoritmo seguro y recomendado.
     *
     * @return PasswordEncoder para codificación de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provee la configuración CORS para permitir solicitudes de dominios específicos.
     * Esta configuración es utilizada por los filtros internos de Spring Security.
     *
     * @return UrlBasedCorsConfigurationSource con las reglas CORS registradas.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
