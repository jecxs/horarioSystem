package com.pontificia.horarioponti.modules.ModalidadEducativa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modalidad_educativa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadEducativa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idModalidad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Integer duracionAnios;

 //   @OneToMany(mappedBy = "modalidad", cascade = CascadeType.ALL)
  //  @JsonIgnore
  //  private List<Carrera> carreras;
}
