package com.pontificia.horarioponti.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationalModalityResponseDTO {

    private UUID id;
    private String name;
    private Integer durationYears;
    private String description;

}

