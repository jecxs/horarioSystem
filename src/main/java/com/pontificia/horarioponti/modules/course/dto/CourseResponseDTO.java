package com.pontificia.horarioponti.modules.course.dto;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import com.pontificia.horarioponti.modules.cycle.dto.CycleResponseDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
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

    private CycleResponseDTO cycle;
    private CareerResponseDto career;
    private EducationalModalityResponseDTO modality;
}