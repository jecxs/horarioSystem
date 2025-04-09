package com.pontificia.horarioponti.dtos;

import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDocenteDTO {
    private DocenteDTO docente;
    private Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> disponibilidadPorDia;
    private Map<DisponibilidadDocente.DiaSemana, List<AsignacionHorarioResponseDTO>> asignacionesPorDia;
}

