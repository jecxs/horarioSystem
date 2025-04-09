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
public class AsignacionHorarioResponseDTO {
    private Long id;
    private CursoDTO curso;
    private GrupoDTO grupo;
    private DocenteDTO docente;
    private AmbienteDTO ambiente;
    private DisponibilidadDocente.DiaSemana diaSemana;
    private AsignacionHorario.TipoSesion tipoSesion;
    private List<BloqueHorarioDTO> bloques;
}