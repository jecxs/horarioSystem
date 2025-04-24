package com.pontificia.horarioponti.auth.controller;

import com.pontificia.horarioponti.auth.dto.*;
import com.pontificia.horarioponti.auth.service.AuthService;
import com.pontificia.horarioponti.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

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
            userService.createUser(
                    request.getUsername(),
                    request.getPassword()
            );
            return ResponseEntity.ok("Docente registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Token inválido"));
        }

        try {
            String username = authService.getUsernameFromToken(token.replace("Bearer ", ""));
            UserInfoResponse userInfo = userService.getUserInfo(username);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Token inválido o expirado"));
        }
    }


}
