package com.pontificia.horarioponti.modules.auth;

import com.pontificia.horarioponti.modules.auth.config.UserService;
import com.pontificia.horarioponti.modules.auth.dto.JwtResponse;
import com.pontificia.horarioponti.modules.auth.dto.LoginRequest;
import com.pontificia.horarioponti.modules.auth.dto.RegisterRequest;
import com.pontificia.horarioponti.modules.auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.modules.educational_modality.dto.ApiResponse;
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

    /**
     * Inicia sesión de un usuario autenticado.
     *
     * @param request Contiene el nombre de usuario y la contraseña.
     * @return Una respuesta con el token JWT y la información del usuario.
     */
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

    /**
     * Registra un nuevo usuario docente en el sistema.
     *
     * @param request Datos del nuevo usuario (nombre, correo, contraseña, etc.).
     * @return Una respuesta con un mensaje de éxito o error.
     */
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

    /**
     * Devuelve la información del usuario autenticado.
     *
     * @param token Token JWT incluido en la cabecera Authorization.
     * @return Una respuesta con los datos del usuario autenticado.
     */
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