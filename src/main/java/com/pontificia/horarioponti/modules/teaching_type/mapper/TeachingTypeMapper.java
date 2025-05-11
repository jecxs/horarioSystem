package com.pontificia.horarioponti.modules.teaching_type.mapper;

import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeachingTypeMapper {
    public TeachingTypeResponseDTO toResponseDTO(TeachingTypeEntity entity) {
        if (entity == null) return null;

        return TeachingTypeResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName().name())
                .build();
    }

    public List<TeachingTypeResponseDTO> toResponseDTOList(List<TeachingTypeEntity> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}