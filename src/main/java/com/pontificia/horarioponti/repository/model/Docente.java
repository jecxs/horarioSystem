package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocente;

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(length = 100)
    private String especialidad;

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL)
    private List<DisponibilidadDocente> disponibilidades;

    @OneToMany(mappedBy = "docente")
    private List<AsignacionHorario> asignaciones;
}
