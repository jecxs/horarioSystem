package com.pontificia.horarioponti.modules.cycle.dto;

import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CycleResponseDTO {
    private UUID uuid;
    private Integer number;
    private CareerResponseDto career;
}
