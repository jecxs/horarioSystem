package com.pontificia.horarioponti.modules.teacher;

import com.pontificia.horarioponti.modules.specialty.SpecialtyEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teacher")
@Getter
@Setter
public class TeacherEntity extends BaseEntity {
    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "document", nullable = false, length = 12, unique = true)
    private String document;

    @Column(name = "phone_number", length = 12)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", referencedColumnName = "uuid")
    private SpecialtyEntity specialty;
}
