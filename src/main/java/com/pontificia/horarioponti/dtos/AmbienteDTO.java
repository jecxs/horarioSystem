package com.pontificia.horarioponti.dtos;

import com.pontificia.horarioponti.repository.model.Ambiente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbienteDTO {
    private Long idAmbiente;
    private String nombre;
    private Ambiente.TipoAmbiente tipo;
    private Integer capacidad;
}
