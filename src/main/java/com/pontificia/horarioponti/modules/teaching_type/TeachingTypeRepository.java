package com.pontificia.horarioponti.modules.teaching_type;

import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachingTypeRepository extends BaseRepository<TeachingTypeEntity> {
    Optional<TeachingTypeEntity> findByName(String name);
    boolean existsByName(String name);

}
