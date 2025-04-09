package com.pontificia.horarioponti.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "asignacion_horario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "ambiente_id", nullable = false)
    private Ambiente ambiente;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private DisponibilidadDocente.DiaSemana diaSemana;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TipoSesion tipoSesion;

    @ManyToMany
    @JoinTable(
            name = "asignacion_horario_bloque",
            joinColumns = @JoinColumn(name = "id_asignacion"),
            inverseJoinColumns = @JoinColumn(name = "id_bloque")
    )
    private List<BloqueHorario> bloques;

    public enum TipoSesion {
        Teorica, Practica
    }
}
