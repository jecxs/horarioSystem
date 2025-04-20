package com.pontificia.horarioponti.auth.service;

import com.pontificia.horarioponti.auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.repository.UserRepository;
import com.pontificia.horarioponti.repository.model.User;
import com.pontificia.horarioponti.enums.Role;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(String username, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }


        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        user.setRole(Role.TEACHER);

        return userRepository.save(user);
    }

    public UserInfoResponse getUserInfo(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> new UserInfoResponse(
                u.getId(),
                u.getUsername(),
                u.getRole()
        )).orElse(null);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }
}