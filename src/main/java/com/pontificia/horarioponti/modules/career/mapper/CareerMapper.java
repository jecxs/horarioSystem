package com.pontificia.horarioponti.modules.career.mapper;

import com.pontificia.horarioponti.modules.career.CareerEntity;
import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;

import java.util.stream.Collectors;

public class CareerMapper {

    public static CareerResponseDto toDto(CareerEntity career) {
        return CareerResponseDto.builder()
                .uuid(career.getUuid())
                .name(career.getName())
                .modality(CareerResponseDto.ModalityDto.builder()
                        .uuid(career.getModality().getUuid())
                        .name(career.getModality().getName())
                        .durationYears(career.getModality().getDurationYears())
                        .build())
                .cycles(career.getCycles().stream()
                        .map(cycle -> CareerResponseDto.CycleDto.builder()
                                .uuid(cycle.getUuid())
                                .number(cycle.getNumber())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}