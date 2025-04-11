package com.pontificia.horarioponti.dtos;

import com.pontificia.horarioponti.repository.model.Curso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long idCurso;
    private String nombre;
    private Curso.TipoCurso tipo;
    private Integer horasSemana;
    private String cicloNombre;
    private Long cicloId; // Agregar este campo
    private String carreraNombre;
    private String modalidadNombre;
}
