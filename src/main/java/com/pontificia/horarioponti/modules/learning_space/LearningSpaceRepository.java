package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositorio para la entidad LearningSpaceEntity.
 * Extiende funcionalidades básicas de BaseRepository.
 */
@Repository
public interface LearningSpaceRepository extends BaseRepository<LearningSpaceEntity> {

    /**
     * Verifica si existe un espacio de aprendizaje con un nombre y tipo específicos.
     *
     * @param name Nombre del espacio de aprendizaje.
     * @param typeUUID Entidad que representa el tipo de enseñanza.
     * @return true si existe un espacio con ese nombre y tipo, false en caso contrario.
     */
    boolean existsByNameAndTypeUUID(String name, TeachingTypeEntity typeUUID);

    /**
     * Verifica si existe un espacio de aprendizaje con un nombre y tipo específicos,
     * excluyendo un registro con un UUID dado.
     *
     * @param name Nombre del espacio de aprendizaje.
     * @param typeUUID Entidad que representa el tipo de enseñanza.
     * @param uuid UUID del espacio de aprendizaje a excluir de la búsqueda.
     * @return true si existe otro espacio con ese nombre y tipo distinto al UUID dado, false en caso contrario.
     */
    boolean existsByNameAndTypeUUIDAndUuidNot(String name, TeachingTypeEntity typeUUID, UUID uuid);
}
