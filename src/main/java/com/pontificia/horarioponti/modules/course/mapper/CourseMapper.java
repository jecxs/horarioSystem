package com.pontificia.horarioponti.modules.course.mapper;


import com.pontificia.horarioponti.modules.course.CourseEntity;
import com.pontificia.horarioponti.modules.course.dto.CourseRequestDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseResponseDTO;
import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.mapper.TeachingTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private final TeachingTypeMapper teachingTypeMapper;

    @Autowired
    public CourseMapper(TeachingTypeMapper teachingTypeMapper) {
        this.teachingTypeMapper = teachingTypeMapper;
    }

    public CourseResponseDTO toResponseDTO(CourseEntity entity) {
        if (entity == null) return null;

        // Determinar el tipo de curso (teórico, práctico o mixto)
        String courseType = entity.isMixed() ? "MIXED" :
                (entity.isTheoretical() ? "THEORETICAL" : "PRACTICAL");

        return CourseResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .weeklyHours(entity.getWeeklyHours())
                .courseType(courseType)
                .teachingTypes(teachingTypeMapper.toResponseDTOList(
                        entity.getTeachingTypes().stream().toList()))
                .cycleUuid(entity.getCycle().getUuid())
                .cycleNumber(String.valueOf(entity.getCycle().getNumber()))
                .careerUuid(entity.getCycle().getCareer().getUuid())
                .careerName(entity.getCycle().getCareer().getName())
                .modalityUuid(entity.getCycle().getCareer().getModality().getUuid())
                .modalityName(entity.getCycle().getCareer().getModality().getName())
                .build();
    }

    public List<CourseResponseDTO> toResponseDTOList(List<CourseEntity> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseEntity toEntity(CourseRequestDTO dto, CycleEntity cycle, Set<TeachingTypeEntity> teachingTypes) {
        if (dto == null) return null;

        CourseEntity entity = new CourseEntity();
        entity.setName(dto.getName());
        entity.setWeeklyHours(dto.getWeeklyHours());
        entity.setCycle(cycle);
        entity.setTeachingTypes(teachingTypes);

        return entity;
    }

    public void updateEntityFromDTO(CourseEntity entity, CourseRequestDTO dto,
                                    CycleEntity cycle, Set<TeachingTypeEntity> teachingTypes) {
        if (entity == null || dto == null) return;

        entity.setName(dto.getName());
        entity.setWeeklyHours(dto.getWeeklyHours());
        entity.setCycle(cycle);
        entity.setTeachingTypes(teachingTypes);
    }
}
