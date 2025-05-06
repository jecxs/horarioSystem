package com.pontificia.horarioponti.modules.Auth.controller;

import com.pontificia.horarioponti.modules.Auth.dto.JwtResponse;
import com.pontificia.horarioponti.modules.Auth.dto.LoginRequest;
import com.pontificia.horarioponti.modules.Auth.dto.RegisterRequest;
import com.pontificia.horarioponti.modules.Auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.modules.Auth.service.AuthService;
import com.pontificia.horarioponti.modules.Auth.service.UserService;
import com.pontificia.horarioponti.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.authenticate(request.getUsername(), request.getPassword());
            UserInfoResponse user = jwtResponse.getUser();

            JwtResponse response = new JwtResponse(jwtResponse.getToken(), user);

            return ResponseEntity.ok(ApiResponse.success(response, "Login exitoso"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerTeacher(@RequestBody @Valid RegisterRequest request) {
        try {
            userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success(null, "Usuario registrado exitosamente"));
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