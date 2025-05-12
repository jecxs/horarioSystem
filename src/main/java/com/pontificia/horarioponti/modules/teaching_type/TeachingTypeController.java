package com.pontificia.horarioponti.modules.teaching_type;


import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import com.pontificia.horarioponti.config.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/teaching-types")
@RequiredArgsConstructor
public class TeachingTypeController {

    private final TeachingTypeService teachingTypeService;

    /**
     * Endpoint para obtener todos los tipos de enseñanza registrados.
     *
     * @return {@link ResponseEntity} con una respuesta estándar {@link ApiResponse}
     *         que contiene una lista de {@link TeachingTypeResponseDTO}.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeachingTypeResponseDTO>>> getAllTeachingTypes() {
        List<TeachingTypeResponseDTO> types = teachingTypeService.getAllTeachingTypes();
        return ResponseEntity.ok(
                ApiResponse.success(types, "Tipos de enseñanza recuperados con éxito")
        );
    }

    /**
     * Endpoint para obtener un tipo de enseñanza específico por su ID.
     *
     * @param uuid Identificador único del tipo de enseñanza.
     * @return {@link ResponseEntity} con una respuesta estándar {@link ApiResponse}
     *         que contiene un {@link TeachingTypeResponseDTO}.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<TeachingTypeResponseDTO>> getTeachingTypeById(
            @PathVariable UUID uuid) {
        TeachingTypeResponseDTO type = teachingTypeService.getTeachingTypeById(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(type, "Tipo de enseñanza recuperado con éxito")
        );
    }
}
