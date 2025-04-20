package com.pontificia.horarioponti.utils.abstractBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
    Optional<T> findByUuid(UUID uuid);

    default boolean existsByUuid(UUID uuid) {
        return findByUuid(uuid).isPresent();
    }
}
