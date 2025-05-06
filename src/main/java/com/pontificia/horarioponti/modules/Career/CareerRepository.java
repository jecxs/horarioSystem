package com.pontificia.horarioponti.modules.Career;

import com.pontificia.horarioponti.modules.EducationalModality.EducationalModality;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends BaseRepository<Career> {

    List<Career> findByModality(EducationalModality modality);

    Optional<Career> findByNameAndModality(String name, EducationalModality modality);

    boolean existsByNameAndModality(String name, EducationalModality modality);

}
