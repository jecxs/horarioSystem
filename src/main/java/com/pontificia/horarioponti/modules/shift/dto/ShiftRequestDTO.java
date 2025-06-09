package com.pontificia.horarioponti.modules.shift.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ShiftRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío") String name,
        @NotNull(message = "La hora de inicio no puede ser nula") LocalTime startTime,
        @NotNull(message = "La hora de fin no puede ser nula") LocalTime endTime
) {
    @AssertTrue(message = "La hora de inicio debe ser anterior a la hora de fin")
    public boolean isStartTimeBeforeEndTime() {
        if (startTime == null || endTime == null) return true;
        return startTime.isBefore(endTime);
    }
}