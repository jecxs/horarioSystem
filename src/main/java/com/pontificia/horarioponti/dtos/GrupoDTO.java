package com.pontificia.horarioponti.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoDTO {
    private Long idGrupo;
    private String nombre;
    private Long cicloId;
    private String cicloNombre;
}

