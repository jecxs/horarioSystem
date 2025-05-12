package com.pontificia.horarioponti.modules.career.mapper;

import com.pontificia.horarioponti.modules.career.CareerEntity;
import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import com.pontificia.horarioponti.modules.cycle.dto.CycleResponseDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CareerMapper {

    /**
     * Convierte una entidad CareerEntity en un DTO CareerResponseDto.
     *
     * @param career Entidad CareerEntity que se va a convertir.
     * @return DTO CareerResponseDto con los datos de la entidad CareerEntity.
     */
    public static CareerResponseDto toDto(CareerEntity career) {
        if (career == null) {
            return null;
        }

        return CareerResponseDto.builder()
                .uuid(career.getUuid())
                .name(career.getName())
                .modality(EducationalModalityResponseDTO.builder()
                        .uuid(career.getModality().getUuid())
                        .name(career.getModality().getName())
                        .durationYears(career.getModality().getDurationYears())
                        .build())
                .cycles((CycleResponseDTO) career.getCycles().stream()
                        .map(cycle -> CycleResponseDTO.builder()
                                .uuid(cycle.getUuid())
                                .number(cycle.getNumber())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}