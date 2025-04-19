package com.pontificia.horarioponti.auth.service;

import com.pontificia.horarioponti.auth.config.JwtUtils;
import com.pontificia.horarioponti.repository.UserRepository;
import com.pontificia.horarioponti.repository.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole().name());
    }

    public String getUsernameFromToken(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    public UUID getUserIdFromToken(String token) {
        return jwtUtils.getUserIdFromToken(token);
    }

    public String getRoleFromToken(String token) {
        return jwtUtils.getRoleFromToken(token);
    }
}