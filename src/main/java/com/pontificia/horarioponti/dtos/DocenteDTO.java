package com.pontificia.horarioponti.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocenteDTO {
    private Long idDocente;
    private String nombreCompleto;
    private String especialidad;
    private List<DisponibilidadDocenteDTO> disponibilidades;
}
