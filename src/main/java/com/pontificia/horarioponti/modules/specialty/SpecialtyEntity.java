package com.pontificia.horarioponti.modules.specialty;

import com.pontificia.horarioponti.utils.abstractBase.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "specialty")
@Getter
@Setter
public class SpecialtyEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
}
