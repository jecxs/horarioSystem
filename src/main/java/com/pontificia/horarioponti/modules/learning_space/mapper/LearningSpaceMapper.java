package com.pontificia.horarioponti.modules.learning_space.mapper;

import com.pontificia.horarioponti.modules.learning_space.LearningSpaceEntity;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceRequestDTO;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceResponseDTO;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LearningSpaceMapper {

    private final TeachingTypeService teachingTypeService;

    public LearningSpaceMapper(TeachingTypeService teachingTypeService) {
        this.teachingTypeService = teachingTypeService;
    }

    /**
     * Convierte una entidad a un DTO de respuesta
     * @param entity Entidad a convertir
     * @return DTO de respuesta
     */
    public LearningSpaceResponseDTO toResponseDTO(LearningSpaceEntity entity) {
        if (entity == null) return null;

        return LearningSpaceResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .typeUUID(entity.getTypeUUID() != null ? entity.getTypeUUID().getUuid() : null)
                .typeName(entity.getTypeUUID() != null ? String.valueOf(entity.getTypeUUID().getName()) : null)
                .build();
    }

    /**
     * Convierte un DTO de solicitud a una entidad para crear
     * @param requestDTO DTO de solicitud
     * @return Entidad nueva
     */
    public LearningSpaceEntity toEntity(LearningSpaceRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        LearningSpaceEntity entity = new LearningSpaceEntity();
        entity.setName(requestDTO.getName());
        entity.setCapacity(requestDTO.getCapacity());

        TeachingTypeEntity type = teachingTypeService.findOrThrow(requestDTO.getTypeUUID());
        entity.setTypeUUID(type);

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del DTO de solicitud
     * @param requestDTO DTO con datos nuevos
     * @param entity Entidad a actualizar
     */
    public void updateEntityFromDTO(LearningSpaceRequestDTO requestDTO, LearningSpaceEntity entity) {
        if (requestDTO == null || entity == null) return;

        entity.setName(requestDTO.getName());
        entity.setCapacity(requestDTO.getCapacity());

        TeachingTypeEntity type = teachingTypeService.findOrThrow(requestDTO.getTypeUUID());
        entity.setTypeUUID(type);
    }

    /**
     * Convierte una lista de entidades a DTO de respuesta
     * @param entities Lista de entidades
     * @return Lista de DTOs
     */
    public List<LearningSpaceResponseDTO> toResponseDTOList(List<LearningSpaceEntity> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
