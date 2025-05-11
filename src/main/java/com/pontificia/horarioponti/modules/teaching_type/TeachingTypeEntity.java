package com.pontificia.horarioponti.modules.teaching_type;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "teaching_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeachingTypeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, unique = true)
    private ETeachingType name;

    // @ManyToMany(mappedBy = "teachingTypes")
    //  private Set<Course> courses = new HashSet<>();
}
