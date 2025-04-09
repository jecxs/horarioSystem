package com.pontificia.horarioponti.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueDisponibleDTO {
    private Long bloqueId;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
}
