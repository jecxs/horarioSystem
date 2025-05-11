package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "learning_space")
@Getter
@Setter
public class LearningSpaceEntity extends BaseEntity {
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id_type", unique = true)
    private TeachingTypeEntity type;
}
