package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "grupo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;

    @Column(nullable = false, length = 10)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ciclo_id", nullable = false)
    private Ciclo ciclo;

    @OneToMany(mappedBy = "grupo")
    private List<AsignacionHorario> asignaciones;
}
