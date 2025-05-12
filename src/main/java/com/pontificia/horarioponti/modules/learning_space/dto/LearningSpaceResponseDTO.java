package com.pontificia.horarioponti.modules.learning_space.dto;

import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class LearningSpaceResponseDTO {
    private UUID uuid;
    private String name;
    private Integer capacity;
    private TeachingTypeResponseDTO teachingType;
}

