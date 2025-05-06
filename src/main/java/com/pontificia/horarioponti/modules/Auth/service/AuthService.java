package com.pontificia.horarioponti.modules.Auth.service;

import com.pontificia.horarioponti.modules.Auth.dto.JwtResponse;
import com.pontificia.horarioponti.modules.Auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.jwt.services.JwtService;
import com.pontificia.horarioponti.modules.User.User;
import com.pontificia.horarioponti.modules.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public JwtResponse authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        UserInfoResponse userInfo = new UserInfoResponse(user.getUuid(), user.getUsername(), user.getRole(), user.getFirstName(), user.getLastName(), user.getDocumentNumber());

        String token = jwtService.generateToken(user.getUuid(), user.getUsername(), user.getRole().name());

        return new JwtResponse(token, userInfo);
    }

    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }
}