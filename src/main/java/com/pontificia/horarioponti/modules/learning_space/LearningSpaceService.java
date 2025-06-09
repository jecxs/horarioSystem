package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.config.GlobalExceptionHandler;
import com.pontificia.horarioponti.config.ValidationTopicException;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceRequestDTO;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceResponseDTO;
import com.pontificia.horarioponti.modules.learning_space.mapper.LearningSpaceMapper;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningSpaceService extends BaseService<LearningSpaceEntity> {

    private final LearningSpaceMapper learningSpaceMapper;
    private final TeachingTypeRepository teachingTypeRepository;
    private final LearningSpaceRepository learningSpaceRepository;

    public LearningSpaceService(LearningSpaceRepository learningSpaceRepository, LearningSpaceMapper learningSpaceMapper, TeachingTypeRepository teachingTypeRepository) {
        super(learningSpaceRepository);
        this.learningSpaceRepository = learningSpaceRepository;
        this.learningSpaceMapper = learningSpaceMapper;
        this.teachingTypeRepository = teachingTypeRepository;
    }

    /**
     * Obtiene una entidad {@link TeachingTypeEntity} a partir del DTO de solicitud,
     * validando que el UUID del tipo de enseñanza exista en la base de datos.
     *
     * @param requestDTO DTO con la información para obtener el tipo de enseñanza.
     * @return La entidad {@link TeachingTypeEntity} correspondiente al UUID proporcionado.
     * @throws IllegalArgumentException si el tipo de enseñanza no existe.
     */
    private TeachingTypeEntity getTeachingTypeEntityFromDTO(LearningSpaceRequestDTO requestDTO) {
        UUID typeUUID = requestDTO.getTypeUuid();
        return teachingTypeRepository.findById(typeUUID)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de enseñanza no encontrado con UUID: " + typeUUID));
    }

    /**
     * Obtiene todos los espacios de aprendizaje registrados en la base de datos,
     * y los convierte en una lista de DTOs para la respuesta.
     *
     * @return Lista de {@link LearningSpaceResponseDTO} con los datos de los espacios de aprendizaje.
     */
    public List<LearningSpaceResponseDTO> getAllLearningSpaces() {
        List<LearningSpaceEntity> modalities = findAll();
        return learningSpaceMapper.toResponseDTOList(modalities);
    }

    /**
     * Crea un nuevo espacio de aprendizaje a partir de la información recibida en el DTO de solicitud.
     * Verifica que no exista otro espacio con el mismo nombre y tipo de enseñanza antes de guardar.
     *
     * @param requestDTO DTO con los datos para crear un nuevo espacio de aprendizaje.
     * @return DTO con la información del espacio de aprendizaje creado.
     * @throws IllegalArgumentException si ya existe un espacio con el mismo nombre y tipo de enseñanza.
     */
    @Transactional
    public LearningSpaceResponseDTO createLearningSpace(LearningSpaceRequestDTO requestDTO) {
        TeachingTypeEntity typeEntity = getTeachingTypeEntityFromDTO(requestDTO);

        if (learningSpaceRepository.existsByNameAndTypeUUID(requestDTO.getName(), typeEntity)) {
            throw new ValidationTopicException("Ya existe un salón con el mismo nombre para este tipo de enseñanza.");
        }

        LearningSpaceEntity modality = learningSpaceMapper.toEntity(requestDTO);
        LearningSpaceEntity savedModality = save(modality);

        return learningSpaceMapper.toResponseDTO(savedModality);
    }

    /**
     * Actualiza un espacio de aprendizaje existente identificado por su UUID con los datos proporcionados.
     * Válida que no exista otro espacio con el mismo nombre y tipo, excluyendo el actual.
     *
     * @param uuid       UUID del espacio de aprendizaje a actualizar.
     * @param requestDTO DTO con los nuevos datos para actualizar el espacio de aprendizaje.
     * @return DTO con la información actualizada del espacio de aprendizaje.
     * @throws IllegalArgumentException si otro espacio con el mismo nombre y tipo ya existe.
     */
    @Transactional
    public LearningSpaceResponseDTO updateLearningSpace(UUID uuid, LearningSpaceRequestDTO requestDTO) {
        TeachingTypeEntity typeEntity = getTeachingTypeEntityFromDTO(requestDTO);

        if (((LearningSpaceRepository) baseRepository).existsByNameAndTypeUUIDAndUuidNot(requestDTO.getName(), typeEntity, uuid)) {
            throw new ValidationTopicException("Ya existe otro salón con el mismo nombre para este tipo de enseñanza.");
        }

        LearningSpaceEntity modality = findOrThrow(uuid);

        learningSpaceMapper.updateEntityFromDTO(requestDTO, modality);
        LearningSpaceEntity updatedModality = update(modality);

        return learningSpaceMapper.toResponseDTO(updatedModality);
    }

    /**
     * Elimina un espacio de aprendizaje identificado por su UUID.
     *
     * @param uuid UUID del espacio de aprendizaje que se desea eliminar.
     */
    @Transactional
    public void deleteLearningSpace(UUID uuid) {
        findOrThrow(uuid);
        deleteById(uuid);
    }
}
