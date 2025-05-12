package com.pontificia.horarioponti.modules.learning_space.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LearningSpaceRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String name;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor que 0")
    private Integer capacity;

    @NotNull(message = "El ID del tipo de enseñanza es obligatorio")
    private UUID typeUUID;
}

