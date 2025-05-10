package com.pontificia.horarioponti.modules.career;

import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import com.pontificia.horarioponti.modules.career.dto.CreateCareerRequestDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/career")
@RequiredArgsConstructor
public class CareerController {
    private final CareerService careerService;

    /**
     * Obtiene todas las carreras registradas en el sistema.
     *
     * @return Lista de carreras junto con un mensaje de éxito.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CareerResponseDto>>> getAllCareers() {
        List<CareerResponseDto> response = careerService.getAllCareers();
        return ResponseEntity.ok(
                ApiResponse.success(response, "Carreras educativas recuperadas con éxito")
        );
    }

    /**
     * Crea una nueva carrera y genera automáticamente sus ciclos según los años de la modalidad.
     *
     * @param request Datos para la creación de la carrera (nombre y UUID de modalidad).
     * @return Carrera creada junto con un mensaje de éxito.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CareerResponseDto>> createCareer(
            @Valid @RequestBody CreateCareerRequestDTO request
    ) {
        CareerResponseDto createdCareer = careerService.createCareer(request.name(), request.modalityId());
        return ResponseEntity.ok(
                ApiResponse.success(createdCareer, "Carrera creada exitosamente con ciclos generados")
        );
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<ApiResponse<CareerResponseDto>> updateCareer(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateCareerRequestDTO request
    ) {
        CareerResponseDto updated = careerService.updateCareer(uuid, request);
        return ResponseEntity.ok(
                ApiResponse.success(updated, "Carrera actualizada correctamente")
        );
    }
}
