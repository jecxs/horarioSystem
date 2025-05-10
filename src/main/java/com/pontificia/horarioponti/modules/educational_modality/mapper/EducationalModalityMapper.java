package com.pontificia.horarioponti.modules.educational_modality.mapper;


import com.pontificia.horarioponti.modules.educational_modality.EducationalModalityEntity;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityRequestDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EducationalModalityMapper {

    /**
     * Convierte una entidad a un DTO de respuesta
     * @param entity Entidad a convertir
     * @return DTO de respuesta
     */
    public EducationalModalityResponseDTO toResponseDTO(EducationalModalityEntity entity) {
        if (entity == null) return null;

        return EducationalModalityResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .durationYears(entity.getDurationYears())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convierte un DTO de solicitud a una entidad para crear
     * @param requestDTO DTO de solicitud
     * @return Entidad nueva
     */
    public EducationalModalityEntity toEntity(EducationalModalityRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        EducationalModalityEntity entity = new EducationalModalityEntity();
        entity.setName(requestDTO.getName());
        entity.setDurationYears(requestDTO.getDurationYears());
        entity.setDescription(requestDTO.getDescription());

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del DTO de solicitud
     * @param requestDTO DTO con datos nuevos
     * @param entity Entidad a actualizar
     */
    public void updateEntityFromDTO(EducationalModalityRequestDTO requestDTO, EducationalModalityEntity entity) {
        if (requestDTO == null || entity == null) return;

        entity.setName(requestDTO.getName());
        entity.setDurationYears(requestDTO.getDurationYears());
        entity.setDescription(requestDTO.getDescription());
    }

    /**
     * Convierte una lista de entidades a DTOs de respuesta
     * @param entities Lista de entidades
     * @return Lista de DTOs
     */
    public List<EducationalModalityResponseDTO> toResponseDTOList(List<EducationalModalityEntity> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
