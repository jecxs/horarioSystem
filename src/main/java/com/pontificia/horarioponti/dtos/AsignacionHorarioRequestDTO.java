package com.pontificia.horarioponti.dtos;

import com.pontificia.horarioponti.repository.model.AsignacionHorario;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionHorarioRequestDTO {
    private Long cursoId;
    private Long grupoId;
    private Long docenteId;
    private Long ambienteId;
    private DisponibilidadDocente.DiaSemana diaSemana;
    private AsignacionHorario.TipoSesion tipoSesion;
    private List<Long> bloqueIds;
}