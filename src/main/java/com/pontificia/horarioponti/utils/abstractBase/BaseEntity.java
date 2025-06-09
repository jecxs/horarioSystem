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

/**
 * Clase base abstracta para todas las entidades persistentes del sistema.
 *
 * <p>Incluye atributos comunes como UUID, fecha de creación y fecha de actualización.
 * También configura listeners para el manejo automático de fechas usando Spring Data JPA.</p>
 *
 * <p>Debe ser extendida por todas las entidades del modelo de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Identificador único universal (UUID) generado automáticamente.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    /**
     * Fecha y hora en la que se creó la entidad.
     * Asignada automáticamente por Spring Data JPA.
     */
    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Fecha y hora de la última modificación de la entidad.
     * Actualizada automáticamente por Spring Data JPA.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;
}
