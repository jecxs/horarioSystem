package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoCurso tipo;

    @Column(nullable = false)
    private Integer horasSemana;

    @ManyToOne
    @JoinColumn(name = "ciclo_id", nullable = false)
    private Ciclo ciclo;

    @OneToMany(mappedBy = "curso")
    private List<AsignacionHorario> asignaciones;

    public enum TipoCurso {
        Teorico, Practico, Mixto
    }
}
