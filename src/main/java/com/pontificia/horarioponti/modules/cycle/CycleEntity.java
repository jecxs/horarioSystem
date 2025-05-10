package com.pontificia.horarioponti.modules.cycle;

import com.pontificia.horarioponti.modules.career.CareerEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cycle")
@Getter
@Setter
public class CycleEntity extends BaseEntity {
    @Column(nullable = false)
    private Integer number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "career_id")
    private CareerEntity career;
}
