package com.pontificia.horarioponti.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueHorarioDTO {
    private Long idBloque;
    private String turnoNombre;
    private Integer orden;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}