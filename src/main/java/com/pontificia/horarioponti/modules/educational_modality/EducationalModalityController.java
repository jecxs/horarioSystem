package com.pontificia.horarioponti.modules.educational_modality;

import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityRequestDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.ApiResponse;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/educational-modalities")
@RequiredArgsConstructor
public class EducationalModalityController {
    @Autowired
    private final EducationalModalityService modalityService;

    /**
     * @return Lista de modalidades educativas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EducationalModalityResponseDTO>>> getAllModalities() {
        List<EducationalModalityResponseDTO> modalities = modalityService.getAllModalities();
        return ResponseEntity.ok(
                ApiResponse.success(modalities, "Modalidades educativas recuperadas con éxito")
        );
    }

    /**
     * @param requestDTO Datos de la modalidad a crear
     * @return Modalidad educativa creada
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EducationalModalityResponseDTO>> createModality(
            @Valid @RequestBody EducationalModalityRequestDTO requestDTO) {
        EducationalModalityResponseDTO createdModality = modalityService.createModality(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdModality, "Modalidad educativa creada con éxito"));
    }

    /**
     * @param uuid       ID de la modalidad a actualizar
     * @param requestDTO Nuevos datos de la modalidad
     * @return Modalidad educativa actualizada
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<EducationalModalityResponseDTO>> updateModality(
            @PathVariable UUID uuid,
            @Valid @RequestBody EducationalModalityRequestDTO requestDTO) {
        EducationalModalityResponseDTO updatedModality = modalityService.updateModality(uuid, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success(updatedModality, "Modalidad educativa actualizada con éxito")
        );
    }

    /**
     * @param uuid ID de la modalidad a eliminar
     * @return Respuesta sin contenido
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteModality(@PathVariable UUID uuid) {
        modalityService.deleteModality(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Modalidad educativa eliminada con éxito")
        );
    }
}
