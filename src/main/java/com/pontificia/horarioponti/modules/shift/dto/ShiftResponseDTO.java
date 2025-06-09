package com.pontificia.horarioponti.modules.shift.dto;

import java.time.LocalTime;
import java.util.UUID;

public record ShiftResponseDTO(
        UUID uuid,
        String code,
        LocalTime startTime,
        LocalTime endTime
) {}