package com.pontificia.horarioponti.modules.Career;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pontificia.horarioponti.modules.ModalidadEducativa.EducationalModality;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "career")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Career extends BaseEntity {

        @Column(nullable = false, length = 100)
        private String name;

        @ManyToOne
        @JoinColumn(name = "modalidad_id", nullable = false)
        private EducationalModality modality;

//        @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL)
//        @JsonIgnore
//        private List<Cycle> cycles;


}
