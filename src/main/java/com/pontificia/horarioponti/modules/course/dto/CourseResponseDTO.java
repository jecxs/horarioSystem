package com.pontificia.horarioponti.modules.course.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponseDTO {
    private UUID uuid;
    private String name;
    private Integer weeklyHours;
    private String courseType; // THEORETICAL, PRACTICAL, MIXED
    private List<TeachingTypeResponseDTO> teachingTypes;

    // Información de ciclo
    private UUID cycleUuid;
    private String cycleNumber;

    // Información de carrera
    private UUID careerUuid;
    private String careerName;

    // Información de modalidad
    private UUID modalityUuid;
    private String modalityName;
}