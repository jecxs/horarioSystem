package com.pontificia.horarioponti.modules.TeachingType;

import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "teaching_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeachingType extends BaseEntity {

    @Column(nullable = false, length = 50, unique = true)
    private String name;


   // @ManyToMany(mappedBy = "teachingTypes")
  //  private Set<Course> courses = new HashSet<>();
}
