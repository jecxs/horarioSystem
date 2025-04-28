package com.pontificia.horarioponti.auth.service;

import com.pontificia.horarioponti.auth.dto.RegisterRequest;
import com.pontificia.horarioponti.auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.modules.User.UserRepository;
import com.pontificia.horarioponti.modules.User.User;
import com.pontificia.horarioponti.auth.enums.Role;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(RegisterRequest userPayload) {

        if (userRepository.existsByUsername(userPayload.getUsername())) {
            throw new RuntimeException("Usuario ya registrado");
        }

        User user = new User();
        user.setUsername(userPayload.getUsername());
        user.setPassword(BCrypt.hashpw(userPayload.getPassword(), BCrypt.gensalt()));
        user.setFirstName(userPayload.getFirstName());
        user.setLastName(userPayload.getLastName());
        user.setDocumentNumber(userPayload.getDocumentNumber());

        user.setRole(Role.TEACHER);

        userRepository.save(user);
    }

    public UserInfoResponse getUserInfo(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> new UserInfoResponse(
                u.getUuid(),
                u.getUsername(),
                u.getRole(),
                u.getFirstName(),
                u.getLastName(),
                u.getDocumentNumber()
        )).orElse(null);
    }
}