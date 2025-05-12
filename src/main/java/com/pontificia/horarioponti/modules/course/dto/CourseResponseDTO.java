package com.pontificia.horarioponti.modules.course.dto;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseResponseDTO {
    private UUID uuid;
    private String name;
    private Integer weeklyHours;
    private ETeachingType courseType;
    private List<TeachingTypeResponseDTO> teachingTypes;

    private UUID cycleUuid;
    private String cycleNumber;

    private UUID careerUuid;
    private String careerName;

    private UUID modalityUuid;
    private String modalityName;
}