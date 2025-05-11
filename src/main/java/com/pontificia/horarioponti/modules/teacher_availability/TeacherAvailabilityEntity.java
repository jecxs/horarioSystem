package com.pontificia.horarioponti.modules.teacher_availability;

import com.pontificia.horarioponti.enums.EWeekday;
import com.pontificia.horarioponti.modules.teacher.TeacherEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "teacher_availability")
@Getter
@Setter
public class TeacherAvailabilityEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EWeekday weekday;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "uuid", nullable = false)
    private TeacherEntity teacher;
}
