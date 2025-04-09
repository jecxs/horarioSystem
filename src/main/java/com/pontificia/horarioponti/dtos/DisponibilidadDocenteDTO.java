package com.pontificia.horarioponti.dtos;

import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadDocenteDTO {
    private Long id;
    private DisponibilidadDocente.DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
