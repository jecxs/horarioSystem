package com.pontificia.horarioponti.modules.shift;

import com.pontificia.horarioponti.config.ApiResponse;
import com.pontificia.horarioponti.modules.shift.dto.ShiftRequestDTO;
import com.pontificia.horarioponti.modules.shift.dto.ShiftResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    /**
     * Obtiene la lista de todos los turnos.
     *
     * @return Lista de DTOs ShiftResponseDTO envuelta en ApiResponse.
     */
    @GetMapping
    public ApiResponse<List<ShiftResponseDTO>> getAllShifts() {
        List<ShiftResponseDTO> shifts = shiftService.getAllShifts();
        return ApiResponse.success(shifts, "Turnos obtenidos correctamente.");
    }

    /**
     * Crea un nuevo turno a partir de la información recibida.
     *
     * @param dto DTO con los datos del turno.
     * @return Turno creado envuelto en ApiResponse.
     */
    @PostMapping
    public ApiResponse<ShiftResponseDTO> createShift(@RequestBody @Valid ShiftRequestDTO dto) {
        ShiftResponseDTO created = shiftService.createShift(dto);
        return ApiResponse.success(created, "Turno creado exitosamente.");
    }

    /**
     * Actualiza un turno existente por UUID.
     *
     * @param uuid UUID del turno.
     * @param dto  Datos nuevos del turno.
     * @return Turno actualizado envuelto en ApiResponse.
     */
    @PutMapping("/{uuid}")
    public ApiResponse<ShiftResponseDTO> updateShift(@PathVariable UUID uuid, @RequestBody @Valid ShiftRequestDTO dto) {
        ShiftResponseDTO updated = shiftService.updateShift(uuid, dto);
        return ApiResponse.success(updated, "Turno actualizado correctamente.");
    }

    /**
     * Elimina un turno existente por UUID.
     *
     * @param uuid UUID del turno.
     * @return Mensaje de éxito envuelto en ApiResponse.
     */
    @DeleteMapping("/{uuid}")
    public ApiResponse<Void> deleteShift(@PathVariable UUID uuid) {
        shiftService.deleteShift(uuid);
        return ApiResponse.success(null, "Turno eliminado correctamente.");
    }
}
