package com.pontificia.horarioponti.modules.time_block;

import com.pontificia.horarioponti.modules.shift.ShiftEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "time_block")
@Getter
@Setter
public class TimeBlockEntity extends BaseEntity {
    @Column(name = "order_in_shift", nullable = false)
    private int orderInShift;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration", nullable = false)
    private int duration = 45;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "uuid", nullable = false)
    private ShiftEntity shift;
}
