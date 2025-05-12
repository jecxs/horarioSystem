package com.pontificia.horarioponti.modules.career.dto;

import com.pontificia.horarioponti.modules.cycle.dto.CycleResponseDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CareerResponseDto {
    private UUID uuid;
    private String name;
    private EducationalModalityResponseDTO modality;
    private CycleResponseDTO cycles;
}