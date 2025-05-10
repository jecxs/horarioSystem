package com.pontificia.horarioponti.modules.educational_modality;


import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "educational_modality")
@Getter
@Setter
public class EducationalModalityEntity extends BaseEntity {

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, name = "duration_years")
    private Integer durationYears;

    @Lob
    private String description;
}