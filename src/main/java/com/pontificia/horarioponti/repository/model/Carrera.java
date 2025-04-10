package com.pontificia.horarioponti.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "carrera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrera;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "modalidad_id", nullable = false)
    private ModalidadEducativa modalidad;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ciclo> ciclos;
}
