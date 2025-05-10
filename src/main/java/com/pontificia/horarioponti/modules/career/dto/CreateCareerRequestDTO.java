package com.pontificia.horarioponti.modules.career.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCareerRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "El ID de la modalidad es obligatorio")
        UUID modalityId
) {
}