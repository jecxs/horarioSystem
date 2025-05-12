package com.pontificia.horarioponti.modules.course.mapper;


import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import com.pontificia.horarioponti.modules.course.CourseEntity;
import com.pontificia.horarioponti.modules.course.dto.CourseRequestDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseResponseDTO;
import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.modules.cycle.dto.CycleResponseDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
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

    /**
     * Convierte una entidad CourseEntity a su DTO correspondiente CourseResponseDTO.
     *
     * @param entity Entidad CourseEntity que se va a convertir.
     * @return DTO CourseResponseDTO que representa la entidad CourseEntity.
     */
    public CourseResponseDTO toResponseDTO(CourseEntity entity) {
        if (entity == null) return null;

        return CourseResponseDTO.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .weeklyHours(entity.getWeeklyHours())
                .teachingTypes(teachingTypeMapper.toResponseDTOList(
                        entity.getTeachingTypes().stream().toList()))
                .teachingTypes(teachingTypeMapper.toResponseDTOList(
                        entity.getTeachingTypes().stream().toList()))
                .cycle(CycleResponseDTO.builder()
                        .uuid(entity.getCycle().getUuid())
                        .number(entity.getCycle().getNumber())
                        .build())
                .career(CareerResponseDto.builder()
                        .uuid(entity.getCycle().getCareer().getUuid())
                        .name(entity.getCycle().getCareer().getName())
                        .build())
                .modality(EducationalModalityResponseDTO.builder()
                        .uuid(entity.getCycle().getCareer().getModality().getUuid())
                        .name(entity.getCycle().getCareer().getModality().getName())
                        .durationYears(entity.getCycle().getCareer().getModality().getDurationYears())
                        .build())
                .build();
    }

    /**
     * Convierte una lista de entidades CourseEntity en una lista de DTOs CourseResponseDTO.
     *
     * @param entities Lista de entidades CourseEntity a convertir.
     * @return Lista de DTOs CourseResponseDTO.
     */
    public List<CourseResponseDTO> toResponseDTOList(List<CourseEntity> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un DTO CourseRequestDTO en una entidad CourseEntity.
     *
     * @param dto           DTO con los datos del curso a convertir.
     * @param cycle         Entidad CycleEntity asociada al curso.
     * @param teachingTypes Set de entidades TeachingTypeEntity asociadas al curso.
     * @return La entidad CourseEntity creada a partir del DTO.
     */
    public CourseEntity toEntity(CourseRequestDTO dto, CycleEntity cycle, Set<TeachingTypeEntity> teachingTypes) {
        if (dto == null) return null;

        CourseEntity entity = new CourseEntity();
        entity.setName(dto.getName());
        entity.setWeeklyHours(dto.getWeeklyHours());
        entity.setCycle(cycle);
        entity.setTeachingTypes(teachingTypes);

        return entity;
    }

    /**
     * Actualiza una entidad CourseEntity con los datos del DTO CourseRequestDTO.
     *
     * @param entity        Entidad CourseEntity a actualizar.
     * @param dto           DTO CourseRequestDTO con los nuevos datos.
     * @param cycle         Entidad CycleEntity asociada al curso.
     * @param teachingTypes Set de entidades TeachingTypeEntity asociadas al curso.
     */
    public void updateEntityFromDTO(CourseEntity entity, CourseRequestDTO dto,
                                    CycleEntity cycle, Set<TeachingTypeEntity> teachingTypes) {
        if (entity == null || dto == null) return;

        entity.setName(dto.getName());
        entity.setWeeklyHours(dto.getWeeklyHours());
        entity.setCycle(cycle);
        entity.setTeachingTypes(teachingTypes);
    }
}
