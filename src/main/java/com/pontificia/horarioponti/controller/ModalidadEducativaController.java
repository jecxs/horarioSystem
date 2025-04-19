package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.payload.response.ApiResponse;
import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import com.pontificia.horarioponti.service.CarreraService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modalidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModalidadEducativaController {

    private final CarreraService carreraService;
    private static final Logger logger = LoggerFactory.getLogger(ModalidadEducativaController.class);

    /**
     * Obtiene todas las modalidades educativas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ModalidadEducativa>>> obtenerTodasModalidades() {
        try {
            List<ModalidadEducativa> modalidades = carreraService.obtenerTodasModalidades();
            return ResponseEntity.ok(ApiResponse.success(modalidades, "Modalidades educativas obtenidas con éxito"));
        } catch (Exception e) {
            logger.error("Error al obtener modalidades educativas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Ocurrió un error inesperado al obtener las modalidades educativas.",
                            e.getMessage()));
        }
    }

    /**
     * Crea una nueva modalidad educativa
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ModalidadEducativa>> crearModalidad(
            @RequestBody ModalidadEducativa modalidad) {
        try {
            ModalidadEducativa nuevaModalidad = carreraService.crearModalidad(
                    modalidad.getNombre(), modalidad.getDuracionAnios());

            if (nuevaModalidad != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(nuevaModalidad, "Modalidad educativa creada con éxito"));
            }

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No se pudo crear la modalidad educativa. El nombre ya existe."));
        } catch (Exception e) {
            logger.error("Error al crear modalidad educativa", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Ocurrió un error inesperado al crear la modalidad educativa.",
                            e.getMessage()));
        }
    }

    /**
     * Obtiene una modalidad por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModalidadEducativa>> obtenerModalidadPorId(@PathVariable Long id) {
        try {
            List<ModalidadEducativa> modalidades = carreraService.obtenerTodasModalidades();

            ModalidadEducativa modalidad = modalidades.stream()
                    .filter(m -> m.getIdModalidad().equals(id))
                    .findFirst()
                    .orElse(null);

            if (modalidad != null) {
                return ResponseEntity.ok(ApiResponse.success(modalidad));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Modalidad educativa no encontrada"));
        } catch (Exception e) {
            logger.error("Error al obtener modalidad por ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Ocurrió un error inesperado al obtener la modalidad educativa.",
                            e.getMessage()));
        }
    }

    /**
     * Actualiza una modalidad existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModalidadEducativa>> actualizarModalidad(
            @PathVariable Long id, @RequestBody ModalidadEducativa modalidad) {
        try {
            ModalidadEducativa modalidadActualizada = carreraService.actualizarModalidad(
                    id, modalidad.getNombre(), modalidad.getDuracionAnios());

            if (modalidadActualizada != null) {
                return ResponseEntity.ok(ApiResponse.success(modalidadActualizada, "Modalidad educativa actualizada con éxito"));
            }

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No se pudo actualizar la modalidad educativa"));
        } catch (Exception e) {
            logger.error("Error al actualizar modalidad con ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Ocurrió un error inesperado al actualizar la modalidad educativa.",
                            e.getMessage()));
        }
    }

    /**
     * Elimina una modalidad
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarModalidad(@PathVariable Long id) {
        try {
            boolean eliminado = carreraService.eliminarModalidad(id);

            if (eliminado) {
                return ResponseEntity.ok(ApiResponse.success(null, "Modalidad educativa eliminada con éxito"));
            }

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No se pudo eliminar la modalidad educativa. Verifique que no tenga carreras asociadas."));
        } catch (Exception e) {
            logger.error("Error al eliminar modalidad con ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Ocurrió un error inesperado al intentar eliminar la modalidad educativa.",
                            e.getMessage()));
        }
    }
}