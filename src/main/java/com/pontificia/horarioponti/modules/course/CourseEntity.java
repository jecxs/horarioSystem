package com.pontificia.horarioponti.modules.course;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
public class CourseEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Integer weeklyHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private CycleEntity cycle;

    @ManyToMany
    @JoinTable(
            name = "course_teaching_type",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teaching_type_id")
    )
    private Set<TeachingTypeEntity> teachingTypes = new HashSet<>();
}
