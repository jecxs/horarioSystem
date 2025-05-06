package com.pontificia.horarioponti.modules.user;


import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE uuid = :uuid", nativeQuery = true)
    Optional<User> findSomethingCustom(@Param("uuid") UUID uuid);
}
