package com.pontificia.horarioponti.modules.ModalidadEducativa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "educational_modality")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalModality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idModality;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer duration_years;


    private Integer description;


 //   @OneToMany(mappedBy = "modalidad", cascade = CascadeType.ALL)
  //  @JsonIgnore
  //  private List<Carrera> carreras;
}
