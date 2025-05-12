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

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningSpaceResponseDTO>>> getAllLearningSpaces() {
        List<LearningSpaceResponseDTO> modalities = learningSpaceService.getAllLearningSpaces();
        return ResponseEntity.ok(
                ApiResponse.success(modalities, "Espacios de aprendizaje recuperadas con éxito")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LearningSpaceResponseDTO>> createLearningSpace(
            @Valid @RequestBody LearningSpaceRequestDTO requestDTO) {
        LearningSpaceResponseDTO createdModality = learningSpaceService.createLearningSpace(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdModality, "Espacio de aprendizaje creada con éxito"));
    }


    @PatchMapping("/{uuid}")
    public ResponseEntity<ApiResponse<LearningSpaceResponseDTO>> updateLearningSpace(
            @PathVariable UUID uuid,
            @Valid @RequestBody LearningSpaceRequestDTO requestDTO) {
        LearningSpaceResponseDTO updatedModality = learningSpaceService.updateLearningSpace(uuid, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success(updatedModality, "Espacio de aprendizaje actualizada con éxito")
        );
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteLearningSpace(@PathVariable UUID uuid) {
        learningSpaceService.deleteLearningSpace(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Espacio de aprendizaje eliminada con éxito")
        );
    }
}
