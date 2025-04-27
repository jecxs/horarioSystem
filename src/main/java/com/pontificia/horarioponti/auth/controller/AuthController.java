package com.pontificia.horarioponti.auth.controller;

import com.pontificia.horarioponti.auth.dto.*;
import com.pontificia.horarioponti.auth.service.AuthService;
import com.pontificia.horarioponti.auth.service.UserService;
import com.pontificia.horarioponti.payload.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        if(token != null) {
            return ResponseEntity.ok(ApiResponse.success(new JwtResponse(token), "Login exitoso"));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Credenciales inválidas"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerTeacher(@RequestBody @Valid RegisterRequest request) {
        try {
            userService.createUser(
                    request.getUsername(),
                    request.getPassword()
            );
            return ResponseEntity.ok(ApiResponse.success("Docente registrado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Token inválido"));
        }

        try {
            String username = authService.getUsernameFromToken(token.replace("Bearer ", ""));
            UserInfoResponse userInfo = userService.getUserInfo(username);
            return ResponseEntity.ok(ApiResponse.success(userInfo, "Información de usuario obtenida con éxito"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token inválido o expirado", e.getMessage()));
        }
    }
}