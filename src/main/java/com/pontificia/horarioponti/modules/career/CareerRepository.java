package com.pontificia.horarioponti.modules.career;

import com.pontificia.horarioponti.modules.educational_modality.EducationalModalityEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends BaseRepository<CareerEntity> {

    List<CareerEntity> findByModality(EducationalModalityEntity modality);

    Optional<CareerEntity> findByNameAndModality(String name, EducationalModalityEntity modality);

    boolean existsByNameAndModality(String name, EducationalModalityEntity modality);

}
