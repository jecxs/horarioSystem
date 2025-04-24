package com.pontificia.horarioponti.modules.ModalidadEducativa;

import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationalModalityRepository extends BaseRepository<EducationalModality> {

    Optional<EducationalModality> findByName(String name);

    boolean existsByName(String name);
}
