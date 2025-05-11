package com.pontificia.horarioponti.modules.auth;

import com.pontificia.horarioponti.modules.auth.dto.JwtResponse;
import com.pontificia.horarioponti.modules.auth.dto.UserInfoResponse;
import com.pontificia.horarioponti.utils.jwt.services.JwtService;
import com.pontificia.horarioponti.modules.user.UserEntity;
import com.pontificia.horarioponti.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Auténtica a un usuario verificando sus credenciales.
     *
     * @param username Nombre de usuario ingresado.
     * @param password Contraseña proporcionada por el usuario.
     * @return Un {@link JwtResponse} que contiene el token generado y la información del usuario.
     * @throws RuntimeException Si el usuario no existe o las credenciales son inválidas.
     */
    public JwtResponse authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        UserInfoResponse userInfo = new UserInfoResponse(user.getUuid(), user.getUsername(), user.getRole(), user.getFirstName(), user.getLastName(), user.getDocumentNumber());

        String token = jwtService.generateToken(user.getUuid(), user.getUsername(), user.getRole().name());

        return new JwtResponse(token, userInfo);
    }

    /**
     * Extrae el nombre de usuario desde un token JWT.
     *
     * @param token Token JWT válido.
     * @return El nombre de usuario contenido en el token.
     */
    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }
}