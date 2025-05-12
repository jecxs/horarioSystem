package com.pontificia.horarioponti.modules.teaching_type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeachingTypeResponseDTO {
    private UUID uuid;
    private String name;
}