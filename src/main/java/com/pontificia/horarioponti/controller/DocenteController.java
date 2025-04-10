package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.dtos.DisponibilidadDocenteDTO;
import com.pontificia.horarioponti.dtos.DocenteDTO;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.service.DisponibilidadDocenteService;
import com.pontificia.horarioponti.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private DisponibilidadDocenteService disponibilidadService;

    /**
     * Vista para listar docentes - La única vista aparte de la principal
     */
    @GetMapping
    public String listarDocentes(Model model) {
        model.addAttribute("docentes", docenteService.obtenerTodosDocentes());
        model.addAttribute("diasSemana", DisponibilidadDocente.DiaSemana.values());
        return "docentes/index";
    }

    /**
     * Obtiene todos los docentes (para uso en modales/selectores)
     */
    @GetMapping("/lista")
    @ResponseBody
    public ResponseEntity<List<DocenteDTO>> obtenerTodosDocentes() {
        return ResponseEntity.ok(docenteService.obtenerTodosDocentes());
    }

    /**
     * Obtiene un docente por ID
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(@PathVariable Long id) {
        DocenteDTO docente = docenteService.obtenerDocentePorId(id);
        if (docente != null) {
            return ResponseEntity.ok(docente);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Busca docentes por nombre
     */
    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<DocenteDTO>> buscarDocentesPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(docenteService.buscarDocentesPorNombre(nombre));
    }

    /**
     * Crea un nuevo docente
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<DocenteDTO> crearDocente(@RequestBody DocenteDTO docenteDTO) {
        DocenteDTO nuevoDocente = docenteService.crearDocente(docenteDTO);
        if (nuevoDocente != null) {
            return ResponseEntity.ok(nuevoDocente);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Actualiza un docente existente
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DocenteDTO> actualizarDocente(
            @PathVariable Long id, @RequestBody DocenteDTO docenteDTO) {
        DocenteDTO docenteActualizado = docenteService.actualizarDocente(id, docenteDTO);
        if (docenteActualizado != null) {
            return ResponseEntity.ok(docenteActualizado);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina un docente
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        boolean eliminado = docenteService.eliminarDocente(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Crea una nueva disponibilidad para un docente
     */
    @PostMapping("/{docenteId}/disponibilidad")
    @ResponseBody
    public ResponseEntity<DisponibilidadDocenteDTO> crearDisponibilidad(
            @PathVariable Long docenteId,
            @RequestParam DisponibilidadDocente.DiaSemana diaSemana,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {

        LocalTime horaInicioTime = LocalTime.parse(horaInicio);
        LocalTime horaFinTime = LocalTime.parse(horaFin);

        DisponibilidadDocenteDTO disponibilidad = disponibilidadService.crearDisponibilidad(
                docenteId, diaSemana, horaInicioTime, horaFinTime);

        if (disponibilidad != null) {
            return ResponseEntity.ok(disponibilidad);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina una disponibilidad
     */
    @DeleteMapping("/disponibilidad/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarDisponibilidad(@PathVariable Long id) {
        boolean eliminado = disponibilidadService.eliminarDisponibilidad(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}