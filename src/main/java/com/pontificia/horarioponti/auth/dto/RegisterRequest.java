package com.pontificia.horarioponti.auth.dto;

import com.pontificia.horarioponti.auth.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(min = 5, max = 100, message = "El correo electrónico debe tener entre 5 y 100 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "El tamaño mínimo de la contraseña es 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "La contraseña debe contener al menos una letra mayúscula, una letra minúscula y un número")
    private String password;

    private Role role;

    @NotBlank(message = "El primer nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "El primer nombre solo puede contener letras")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "El apellido solo puede contener letras")
    private String lastName;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Pattern(regexp = "^\\d{8}$", message = "El número de documento debe ser un número de 8 dígitos")
    private String documentNumber;
}
