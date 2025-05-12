package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceRequestDTO;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceResponseDTO;
import com.pontificia.horarioponti.modules.learning_space.mapper.LearningSpaceMapper;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningSpaceService extends BaseService<LearningSpaceEntity> {
    private final LearningSpaceMapper learningSpaceMapper;

    public LearningSpaceService(LearningSpaceRepository learningSpaceRepository, LearningSpaceMapper learningSpaceMapper) {
        super(learningSpaceRepository);
        this.learningSpaceMapper = learningSpaceMapper;
    }

    /**
     * Obtiene todos los espacios de aprendizaje y los convierte a un formato de respuesta (DTO).
     *
     * @return Lista de DTOs de respuesta de espacios de aprendizaje.
     */
    public List<LearningSpaceResponseDTO> getAllLearningSpaces() {
        List<LearningSpaceEntity> modalities = findAll();
        return learningSpaceMapper.toResponseDTOList(modalities);
    }

    /**
     * Crea un nuevo espacio de aprendizaje con los datos proporcionados en el DTO.
     *
     * @param requestDTO DTO con los datos necesarios para crear el espacio de aprendizaje.
     * @return DTO de respuesta con los detalles del espacio de aprendizaje creado.
     */
    @Transactional
    public LearningSpaceResponseDTO createLearningSpace(LearningSpaceRequestDTO requestDTO) {
        LearningSpaceEntity modality = learningSpaceMapper.toEntity(requestDTO);
        LearningSpaceEntity savedModality = save(modality);

        return learningSpaceMapper.toResponseDTO(savedModality);
    }

    /**
     * Actualiza un espacio de aprendizaje existente con los datos proporcionados en el DTO.
     *
     * @param uuid       UUID del espacio de aprendizaje a actualizar.
     * @param requestDTO DTO con los nuevos datos del espacio de aprendizaje.
     * @return DTO de respuesta con los detalles del espacio de aprendizaje actualizado.
     */
    @Transactional
    public LearningSpaceResponseDTO updateLearningSpace(UUID uuid, LearningSpaceRequestDTO requestDTO) {
        LearningSpaceEntity modality = findOrThrow(uuid);

        learningSpaceMapper.updateEntityFromDTO(requestDTO, modality);
        LearningSpaceEntity updatedModality = update(modality);

        return learningSpaceMapper.toResponseDTO(updatedModality);
    }

    /**
     * Elimina un espacio de aprendizaje dado su UUID.
     *
     * @param uuid UUID del espacio de aprendizaje a eliminar.
     */
    @Transactional
    public void deleteLearningSpace(UUID uuid) {
        findOrThrow(uuid);
        deleteById(uuid);
    }
}
