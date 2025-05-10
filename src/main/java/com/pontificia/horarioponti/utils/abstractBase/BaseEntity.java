package com.pontificia.horarioponti.utils.abstractBase;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;
}
