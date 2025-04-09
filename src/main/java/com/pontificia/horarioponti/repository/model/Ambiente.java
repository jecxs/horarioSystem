package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ambiente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAmbiente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoAmbiente tipo;

    @Column(nullable = false)
    private Integer capacidad;

    @OneToMany(mappedBy = "ambiente")
    private List<AsignacionHorario> asignaciones;

    public enum TipoAmbiente {
        Teorico, Practico
    }
}
