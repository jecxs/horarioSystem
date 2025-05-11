package com.pontificia.horarioponti.modules.student_group;

import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student_group")
@Getter
@Setter
public class StudentGroupEntity extends BaseEntity {
    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "cycle_id", referencedColumnName = "uuid")
    private CycleEntity cycle;
}
