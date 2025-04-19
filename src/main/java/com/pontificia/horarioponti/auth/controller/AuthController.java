package com.pontificia.horarioponti.auth.controller;

import com.pontificia.horarioponti.auth.dto.*;
import com.pontificia.horarioponti.auth.service.AuthService;
import com.pontificia.horarioponti.auth.service.UserService;
import com.pontificia.horarioponti.repository.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        if(token != null) {
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Credenciales inválidas"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(@RequestBody @Valid RegisterRequest request) {
        try {
            User user = userService.createUser(
                    request.getUsername(),
                    request.getPassword()
            );
            return ResponseEntity.ok("Docente registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        String username = authService.getUsernameFromToken(token.replace("Bearer ", ""));
        UserInfoResponse userInfo = userService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }

}
