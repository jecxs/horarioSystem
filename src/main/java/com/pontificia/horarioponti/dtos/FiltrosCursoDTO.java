package com.pontificia.horarioponti.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosCursoDTO {
    private Long modalidadId;
    private Long carreraId;
    private Long cicloId;
    private String nombreCurso;
}