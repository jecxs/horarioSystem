package com.pontificia.horarioponti.modules.TeachingType;


import com.pontificia.horarioponti.payload.response.ApiResponse;
import com.pontificia.horarioponti.payload.response.TeachingTypeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teaching-types")
public class TeachingTypeController {

    private final TeachingTypeService teachingTypeService;

    @Autowired
    public TeachingTypeController(TeachingTypeService teachingTypeService) {
        this.teachingTypeService = teachingTypeService;
    }

    /**
     * Obtiene todos los tipos de enseñanza
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeachingTypeResponseDTO>>> getAllTeachingTypes() {
        List<TeachingTypeResponseDTO> types = teachingTypeService.getAllTeachingTypes();
        return ResponseEntity.ok(
                ApiResponse.success(types, "Tipos de enseñanza recuperados con éxito")
        );
    }

    /**
     * Obtiene un tipo de enseñanza por ID
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
