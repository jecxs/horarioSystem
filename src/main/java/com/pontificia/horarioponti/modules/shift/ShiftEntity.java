package com.pontificia.horarioponti.modules.shift;

import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "shift")
@Getter
@Setter
public class ShiftEntity extends BaseEntity {
    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
