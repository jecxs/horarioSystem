package com.pontificia.horarioponti.modules.Auth.service;

import com.pontificia.horarioponti.modules.Auth.dto.RegisterRequest;
import com.pontificia.horarioponti.modules.Auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.modules.Auth.enums.Role;
import com.pontificia.horarioponti.modules.User.User;
import com.pontificia.horarioponti.modules.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDocumentNumber(request.getDocumentNumber());

        if (request.getRole() == null) {
            user.setRole(Role.TEACHER);
        } else {
            user.setRole(request.getRole());
        }

        userRepository.save(user);
    }

    public UserInfoResponse getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserInfoResponse(
                user.getUuid(),
                user.getUsername(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getDocumentNumber()
        );
    }
}