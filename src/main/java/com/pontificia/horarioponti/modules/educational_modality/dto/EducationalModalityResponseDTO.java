package com.pontificia.horarioponti.modules.educational_modality.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationalModalityResponseDTO {
    private UUID uuid;
    private String name;
    private Integer durationYears;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}

