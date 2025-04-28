package com.pontificia.horarioponti.auth.service;

import com.pontificia.horarioponti.auth.config.JwtUtils;
import com.pontificia.horarioponti.auth.dto.JwtResponse;
import com.pontificia.horarioponti.auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.modules.User.User;
import com.pontificia.horarioponti.modules.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    public JwtResponse authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        UserInfoResponse userInfo = new UserInfoResponse(user.getUuid(), user.getUsername(), user.getRole(), user.getFirstName(), user.getLastName(), user.getDocumentNumber());

        String token = jwtUtils.generateToken(user.getUuid(), user.getUsername(), user.getRole().name());

        return new JwtResponse(token, userInfo);
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