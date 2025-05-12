package com.pontificia.horarioponti.modules.educational_modality;

import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityRequestDTO;
import com.pontificia.horarioponti.config.ApiResponse;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/educational-modalities")
@RequiredArgsConstructor
public class EducationalModalityController {

    private final EducationalModalityService modalityService;

    /**
     * Obtiene todas las modalidades educativas registradas.
     *
     * @return {@link ResponseEntity} con una lista de {@link EducationalModalityResponseDTO}
     *         y un mensaje de éxito dentro de un objeto {@link ApiResponse}.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EducationalModalityResponseDTO>>> getAllModalities() {
        List<EducationalModalityResponseDTO> modalities = modalityService.getAllModalities();
        return ResponseEntity.ok(
                ApiResponse.success(modalities, "Modalidades educativas recuperadas con éxito")
        );
    }

    /**
     * Crea una nueva modalidad educativa a partir del objeto recibido en el cuerpo de la solicitud.
     *
     * @param requestDTO Objeto con los datos de la nueva modalidad.
     * @return {@link ResponseEntity} con el objeto creado {@link EducationalModalityResponseDTO}
     *         y un mensaje de éxito dentro de un objeto {@link ApiResponse}.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EducationalModalityResponseDTO>> createModality(
            @Valid @RequestBody EducationalModalityRequestDTO requestDTO) {
        EducationalModalityResponseDTO createdModality = modalityService.createModality(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdModality, "Modalidad educativa creada con éxito"));
    }

    /**
     * Actualiza una modalidad educativa existente.
     *
     * @param uuid       Identificador único de la modalidad a actualizar.
     * @param requestDTO Nuevos datos que reemplazarán a los actuales.
     * @return {@link ResponseEntity} con el objeto actualizado {@link EducationalModalityResponseDTO}
     *         y un mensaje de éxito dentro de un objeto {@link ApiResponse}.
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<ApiResponse<EducationalModalityResponseDTO>> updateModality(
            @PathVariable UUID uuid,
            @Valid @RequestBody EducationalModalityRequestDTO requestDTO) {
        EducationalModalityResponseDTO updatedModality = modalityService.updateModality(uuid, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success(updatedModality, "Modalidad educativa actualizada con éxito")
        );
    }

    /**
     * Elimina una modalidad educativa identificada por su UUID.
     *
     * @param uuid Identificador único de la modalidad a eliminar.
     * @return {@link ResponseEntity} sin contenido, pero con mensaje de éxito dentro de {@link ApiResponse}.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteModality(@PathVariable UUID uuid) {
        modalityService.deleteModality(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Modalidad educativa eliminada con éxito")
        );
    }
}
