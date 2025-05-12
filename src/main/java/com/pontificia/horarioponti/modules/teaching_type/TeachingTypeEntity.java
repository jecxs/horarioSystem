package com.pontificia.horarioponti.modules.teaching_type;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.course.CourseEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "teaching_type")
@Getter
@Setter
public class TeachingTypeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, unique = true)
    private ETeachingType name;

    @ManyToMany(mappedBy = "teachingTypes")
    private Set<CourseEntity> courses = new HashSet<>();
}
