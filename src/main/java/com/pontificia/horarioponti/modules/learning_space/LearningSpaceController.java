package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.config.ApiResponse;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceRequestDTO;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/learning-space")
@RequiredArgsConstructor
public class LearningSpaceController {
    private final LearningSpaceService learningSpaceService;


    /**
     * Obtiene todos los espacios de aprendizaje.
     *
     * @return Respuesta con lista de DTOs de espacios de aprendizaje.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningSpaceResponseDTO>>> getAllLearningSpaces() {
        List<LearningSpaceResponseDTO> modalities = learningSpaceService.getAllLearningSpaces();
        return ResponseEntity.ok(
                ApiResponse.success(modalities, "Espacios de aprendizaje recuperadas con éxito")
        );
    }

    /**
     * Crea un nuevo espacio de aprendizaje.
     *
     * @param requestDTO DTO con los datos para crear el espacio.
     * @return Respuesta con el DTO del espacio creado.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LearningSpaceResponseDTO>> createLearningSpace(
            @Valid @RequestBody LearningSpaceRequestDTO requestDTO) {
        LearningSpaceResponseDTO createdModality = learningSpaceService.createLearningSpace(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdModality, "Espacio de aprendizaje creada con éxito"));
    }

    /**
     * Actualiza un espacio de aprendizaje existente.
     *
     * @param uuid       UUID del espacio de aprendizaje a actualizar.
     * @param requestDTO DTO con los nuevos datos para actualizar el espacio.
     * @return Respuesta con el DTO del espacio actualizado.
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<ApiResponse<LearningSpaceResponseDTO>> updateLearningSpace(
            @PathVariable UUID uuid,
            @Valid @RequestBody LearningSpaceRequestDTO requestDTO) {
        LearningSpaceResponseDTO updatedModality = learningSpaceService.updateLearningSpace(uuid, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success(updatedModality, "Espacio de aprendizaje actualizada con éxito")
        );
    }

    /**
     * Elimina un espacio de aprendizaje por su UUID.
     *
     * @param uuid UUID del espacio de aprendizaje a eliminar.
     * @return Respuesta con un mensaje de éxito.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteLearningSpace(@PathVariable UUID uuid) {
        learningSpaceService.deleteLearningSpace(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Espacio de aprendizaje eliminada con éxito")
        );
    }
}
