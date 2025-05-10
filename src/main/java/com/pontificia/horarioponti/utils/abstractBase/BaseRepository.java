package com.pontificia.horarioponti.utils.abstractBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
    Optional<T> findByUuid(UUID uuid);

    default boolean existsByUuid(UUID uuid) {
        return findByUuid(uuid).isPresent();
    }

    @Query(value = "SELECT * FROM users WHERE uuid = :uuid", nativeQuery = true)
    Optional<T> findSomethingCustom(@Param("uuid") UUID uuid);
}
