package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.dtos.AmbienteDTO;
import com.pontificia.horarioponti.dtos.BloqueHorarioDTO;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Turno;
import com.pontificia.horarioponti.service.AmbienteService;
import com.pontificia.horarioponti.service.BloqueHorarioService;
import com.pontificia.horarioponti.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/ambientes")
public class AmbienteController {

    @Autowired
    private AmbienteService ambienteService;

    @Autowired
    private BloqueHorarioService bloqueService;

    @Autowired
    private TurnoService turnoService;

    /**
     * Obtiene todos los ambientes
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<AmbienteDTO>> obtenerTodosAmbientes() {
        return ResponseEntity.ok(ambienteService.obtenerTodosAmbientes());
    }

    /**
     * Obtiene un ambiente por ID
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AmbienteDTO> obtenerAmbientePorId(@PathVariable Long id) {
        AmbienteDTO ambiente = ambienteService.obtenerAmbientePorId(id);
        if (ambiente != null) {
            return ResponseEntity.ok(ambiente);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuevo ambiente
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<AmbienteDTO> crearAmbiente(@RequestBody AmbienteDTO ambienteDTO) {
        AmbienteDTO nuevoAmbiente = ambienteService.crearAmbiente(ambienteDTO);
        if (nuevoAmbiente != null) {
            return ResponseEntity.ok(nuevoAmbiente);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Actualiza un ambiente existente
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AmbienteDTO> actualizarAmbiente(
            @PathVariable Long id, @RequestBody AmbienteDTO ambienteDTO) {

        AmbienteDTO ambienteActualizado = ambienteService.actualizarAmbiente(id, ambienteDTO);
        if (ambienteActualizado != null) {
            return ResponseEntity.ok(ambienteActualizado);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina un ambiente
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarAmbiente(@PathVariable Long id) {
        boolean eliminado = ambienteService.eliminarAmbiente(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todos los bloques de horario
     */
    @GetMapping("/bloques")
    @ResponseBody
    public ResponseEntity<List<BloqueHorarioDTO>> obtenerTodosBloques() {
        return ResponseEntity.ok(bloqueService.obtenerTodosBloques());
    }

    /**
     * Obtiene bloques por turno
     */
    @GetMapping("/bloques/turno/{turnoId}")
    @ResponseBody
    public ResponseEntity<List<BloqueHorarioDTO>> obtenerBloquesPorTurno(@PathVariable Long turnoId) {
        return ResponseEntity.ok(bloqueService.obtenerBloquesPorTurno(turnoId));
    }

    /**
     * Obtiene bloques disponibles para un ambiente en un día específico
     */
    @GetMapping("/bloques/disponibles")
    @ResponseBody
    public ResponseEntity<List<BloqueHorarioDTO>> obtenerBloquesDisponibles(
            @RequestParam Long ambienteId,
            @RequestParam DisponibilidadDocente.DiaSemana dia) {

        return ResponseEntity.ok(
                bloqueService.obtenerBloquesDisponiblesPorAmbienteYDia(ambienteId, dia));
    }

    /**
     * Crea bloques de horario para un turno
     */
    @PostMapping("/bloques/generar")
    @ResponseBody
    public ResponseEntity<List<BloqueHorarioDTO>> generarBloques(
            @RequestParam Long turnoId,
            @RequestParam Integer duracionMinutos) {

        List<BloqueHorarioDTO> bloques = bloqueService.crearBloquesPorTurno(turnoId, duracionMinutos);
        if (!bloques.isEmpty()) {
            return ResponseEntity.ok(bloques);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todos los turnos
     */
    @GetMapping("/turnos")
    @ResponseBody
    public ResponseEntity<List<Turno>> obtenerTodosTurnos() {
        return ResponseEntity.ok(turnoService.obtenerTodosTurnos());
    }

    /**
     * Crea un nuevo turno
     */
    @PostMapping("/turnos")
    @ResponseBody
    public ResponseEntity<Turno> crearTurno(
            @RequestParam String nombre,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {

        LocalTime horaInicioTime = LocalTime.parse(horaInicio);
        LocalTime horaFinTime = LocalTime.parse(horaFin);

        Turno nuevoTurno = turnoService.crearTurno(nombre, horaInicioTime, horaFinTime);
        if (nuevoTurno != null) {
            return ResponseEntity.ok(nuevoTurno);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Actualiza un turno existente
     */
    @PutMapping("/turnos/{id}")
    @ResponseBody
    public ResponseEntity<Turno> actualizarTurno(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {

        LocalTime horaInicioTime = LocalTime.parse(horaInicio);
        LocalTime horaFinTime = LocalTime.parse(horaFin);

        Turno turnoActualizado = turnoService.actualizarTurno(id, nombre, horaInicioTime, horaFinTime);
        if (turnoActualizado != null) {
            return ResponseEntity.ok(turnoActualizado);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina un turno
     */
    @DeleteMapping("/turnos/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        boolean eliminado = turnoService.eliminarTurno(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
