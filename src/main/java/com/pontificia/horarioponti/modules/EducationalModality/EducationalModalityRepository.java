package com.pontificia.horarioponti.modules.EducationalModality;

import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalModalityRepository extends BaseRepository<EducationalModality> {
    boolean existsByName(String name);
}
