package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.dtos.*;
import com.pontificia.horarioponti.repository.model.Ambiente;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private AsignacionHorarioService asignacionService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private AmbienteService ambienteService;

    @Autowired
    private BloqueHorarioService bloqueService;

    @Autowired
    private CarreraService carreraService;

    /**
     * Vista principal del sistema - Tablero de horarios
     */
    @GetMapping
    public String vistaHorarios(Model model) {
        // Cargar lista de docentes para el selector
        model.addAttribute("docentes", docenteService.obtenerTodosDocentes());

        // Cargar modalidades para filtros
        model.addAttribute("modalidades", carreraService.obtenerTodasModalidades());

        // Bloques de horario para la tabla
        model.addAttribute("bloques", bloqueService.obtenerTodosBloques());

        // Días de la semana
        model.addAttribute("diasSemana", DisponibilidadDocente.DiaSemana.values());

        // Tipos de ambientes
        model.addAttribute("tiposAmbiente", Ambiente.TipoAmbiente.values());

        return "horarios/index";
    }

    /**
     * Obtiene el horario de un docente específico para cargar en el tablero
     */
    @GetMapping("/docente/{id}")
    @ResponseBody
    public ResponseEntity<HorarioDocenteDTO> obtenerHorarioDocente(@PathVariable Long id) {
        HorarioDocenteDTO horario = asignacionService.obtenerHorarioDocente(id);
        if (horario != null) {
            return ResponseEntity.ok(horario);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea una nueva asignación de horario
     */
    @PostMapping("/asignacion")
    @ResponseBody
    public ResponseEntity<?> crearAsignacion(@RequestBody AsignacionHorarioRequestDTO requestDTO) {
        try {
            AsignacionHorarioResponseDTO asignacion = asignacionService.crearAsignacionHorario(requestDTO);
            if (asignacion != null) {
                return ResponseEntity.ok(asignacion);
            }
            return ResponseEntity.badRequest().body("No se pudo crear la asignación. Verifique los datos.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la asignación: " + e.getMessage());
        }
    }

    /**
     * Elimina una asignación de horario
     */
    @DeleteMapping("/asignacion/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long id) {
        boolean eliminado = asignacionService.eliminarAsignacionHorario(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene cursos filtrados para el modal de asignación
     */
    @GetMapping("/cursos/filtrar")
    @ResponseBody
    public ResponseEntity<List<CursoDTO>> filtrarCursos(FiltrosCursoDTO filtros) {
        List<CursoDTO> cursos = cursoService.filtrarCursos(filtros);
        return ResponseEntity.ok(cursos);
    }

    /**
     * Obtiene grupos disponibles por ciclo
     */
    @GetMapping("/grupos/ciclo/{cicloId}")
    @ResponseBody
    public ResponseEntity<List<GrupoDTO>> obtenerGruposPorCiclo(@PathVariable Long cicloId) {
        List<GrupoDTO> grupos = grupoService.obtenerGruposPorCiclo(cicloId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene ambientes filtrados por tipo y capacidad
     */
    @GetMapping("/ambientes")
    @ResponseBody
    public ResponseEntity<List<AmbienteDTO>> obtenerAmbientes(
            @RequestParam(required = false) Ambiente.TipoAmbiente tipo,
            @RequestParam(required = false) Integer capacidadMinima) {

        List<AmbienteDTO> ambientes;

        if (tipo != null && capacidadMinima != null) {
            ambientes = ambienteService.obtenerAmbientesPorTipoYCapacidadMinima(tipo, capacidadMinima);
        } else if (tipo != null) {
            ambientes = ambienteService.obtenerAmbientesPorTipo(tipo);
        } else if (capacidadMinima != null) {
            ambientes = ambienteService.obtenerAmbientesPorCapacidadMinima(capacidadMinima);
        } else {
            ambientes = ambienteService.obtenerTodosAmbientes();
        }

        return ResponseEntity.ok(ambientes);
    }

    /**
     * Verifica disponibilidad para una posible asignación (antes de crear)
     */
    @PostMapping("/verificar-disponibilidad")
    @ResponseBody
    public ResponseEntity<Boolean> verificarDisponibilidad(@RequestBody AsignacionHorarioRequestDTO requestDTO) {
        boolean docenteDisponible = asignacionService.verificarDisponibilidadDocente(
                requestDTO.getDocenteId(), requestDTO.getDiaSemana(), requestDTO.getBloqueIds());

        boolean ambienteDisponible = asignacionService.verificarDisponibilidadAmbiente(
                requestDTO.getAmbienteId(), requestDTO.getDiaSemana(), requestDTO.getBloqueIds());

        boolean grupoDisponible = asignacionService.verificarDisponibilidadGrupo(
                requestDTO.getGrupoId(), requestDTO.getDiaSemana(), requestDTO.getBloqueIds());

        boolean disponible = docenteDisponible && ambienteDisponible && grupoDisponible;

        return ResponseEntity.ok(disponible);
    }
}
