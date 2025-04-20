package com.pontificia.horarioponti.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationalModalityRequestDTO {

    @NotBlank(message = "El nombre no debe estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotNull(message = "La duración en años es obligatoria")
    @Min(value = 1, message = "La duración debe ser como mínimo 1 año")
    @Max(value = 5, message = "La duración debe ser como máximo 5 años")
    private Integer durationYears;

    private String description;
}

