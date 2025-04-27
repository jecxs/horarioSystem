package com.pontificia.horarioponti.modules.Career;

import com.pontificia.horarioponti.modules.ModalidadEducativa.EducationalModality;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends BaseRepository<Career> {

    List<Career> findByModality(EducationalModality modalidad);

    Optional<Career> findByNombreAndModality(String nombre, EducationalModality modalidad);

    boolean existsByNameAndModality(String nombre, EducationalModality modalidad);

}
