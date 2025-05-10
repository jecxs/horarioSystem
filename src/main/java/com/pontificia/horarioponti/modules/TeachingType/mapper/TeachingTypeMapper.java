package com.pontificia.horarioponti.modules.TeachingType.mapper;

import com.pontificia.horarioponti.modules.TeachingType.TeachingType;
import com.pontificia.horarioponti.modules.TeachingType.dto.TeachingTypeResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeachingTypeMapper {
    public TeachingTypeResponseDTO toResponseDTO(TeachingType entity) {
        if (entity == null) return null;

        return TeachingTypeResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .build();
    }

    public List<TeachingTypeResponseDTO> toResponseDTOList(List<TeachingType> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}