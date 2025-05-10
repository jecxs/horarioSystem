package com.pontificia.horarioponti.modules.career.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerResponseDto {
    private UUID uuid;
    private String name;
    private ModalityDto modality;
    private List<CycleDto> cycles;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModalityDto {
        private UUID uuid;
        private String name;
        private int durationYears;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CycleDto {
        private UUID uuid;
        private int number;
    }
}