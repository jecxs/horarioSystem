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
@NoArgsConstructor
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

    /*
    @OneToMany(mappedBy = "course")
    private Set<ScheduleAssignmentEntity> assignments = new HashSet<>();
    */


    /* Verifica si el curso es de tipo mixto (contiene tanto teoría como práctica) */
    @Transient
    public boolean isMixed() {
        if (teachingTypes == null || teachingTypes.isEmpty()) {
            return false;
        }

        boolean hasTheory = false;
        boolean hasPractice = false;

        for (TeachingTypeEntity type : teachingTypes) {
            if (type.getName() == ETeachingType.THEORY) {
                hasTheory = true;
            } else if (type.getName() == ETeachingType.PRACTICE) {
                hasPractice = true;
            }

            if (hasTheory && hasPractice) {
                return true;
            }
        }

        return false;
    }

    /* Verifica si el curso es solo de tipo teórico */
    @Transient
    public boolean isTheoretical() {
        if (teachingTypes == null || teachingTypes.isEmpty()) {
            return false;
        }

        boolean hasTheory = false;
        boolean hasPractice = false;

        for (TeachingTypeEntity type : teachingTypes) {
            if (type.getName() == ETeachingType.THEORY) {
                hasTheory = true;
            } else if (type.getName() == ETeachingType.PRACTICE) {
                hasPractice = true;
            }
        }

        return hasTheory && !hasPractice;
    }

    /* Verifica si el curso es solo de tipo práctico */
    @Transient
    public boolean isPractical() {
        if (teachingTypes == null || teachingTypes.isEmpty()) {
            return false;
        }

        boolean hasTheory = false;
        boolean hasPractice = false;

        for (TeachingTypeEntity type : teachingTypes) {
            if (type.getName() == ETeachingType.THEORY) {
                hasTheory = true;
            } else if (type.getName() == ETeachingType.PRACTICE) {
                hasPractice = true;
            }
        }

        return !hasTheory && hasPractice;
    }
}
