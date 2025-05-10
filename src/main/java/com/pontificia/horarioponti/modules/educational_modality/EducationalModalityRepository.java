package com.pontificia.horarioponti.modules.educational_modality;

import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalModalityRepository extends BaseRepository<EducationalModalityEntity> {
    boolean existsByName(String name);
}
