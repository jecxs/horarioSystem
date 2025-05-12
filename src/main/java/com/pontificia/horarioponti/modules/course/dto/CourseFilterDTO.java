package com.pontificia.horarioponti.modules.course.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseFilterDTO {
    private UUID modalityUuid;
    private UUID careerUuid;
    private UUID cycleUuid;
    private String courseName;
}