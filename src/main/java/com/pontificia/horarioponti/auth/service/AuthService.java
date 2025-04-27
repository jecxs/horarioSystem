package com.pontificia.horarioponti.auth.service;

import com.pontificia.horarioponti.auth.config.JwtUtils;
import com.pontificia.horarioponti.modules.User.UserRepository;
import com.pontificia.horarioponti.modules.User.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtils.generateToken(user.getUuid(), user.getUsername(), user.getRole().name());
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