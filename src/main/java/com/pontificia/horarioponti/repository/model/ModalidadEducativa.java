package com.pontificia.horarioponti.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "modalidad_educativa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadEducativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModalidad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Integer duracionAnios;

    @OneToMany(mappedBy = "modalidad", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Carrera> carreras;
}