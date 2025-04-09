package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ciclo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciclo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCiclo;

    @Column(nullable = false)
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @OneToMany(mappedBy = "ciclo", cascade = CascadeType.ALL)
    private List<Curso> cursos;

    @OneToMany(mappedBy = "ciclo", cascade = CascadeType.ALL)
    private List<Grupo> grupos;
}
