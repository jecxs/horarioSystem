package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.dtos.*;
import com.pontificia.horarioponti.repository.BloqueHorarioRepository;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.CursoRepository;
import com.pontificia.horarioponti.repository.model.*;
import com.pontificia.horarioponti.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private BloqueHorarioRepository bloqueRepository;

    @Autowired
    private CicloRepository cicloRepository;

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
        } catch (IllegalArgumentException e) {
            // Errores de validación como incompatibilidad de tipos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Errores de horas excedidas
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
    /**
     * Verifica las horas asignadas vs requeridas para un curso y grupo
     */
    @GetMapping("/verificar-horas-curso")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> verificarHorasCurso(
            @RequestParam Long cursoId, @RequestParam Long grupoId) {

        // Buscar el curso
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (!cursoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Curso curso = cursoOpt.get();
        int horasAsignadas = asignacionService.calcularHorasAsignadas(cursoId, grupoId);
        int horasTotales = curso.getHorasSemana();
        int horasFaltantes = horasTotales - horasAsignadas;

        Map<String, Integer> resultado = new HashMap<>();
        resultado.put("horasAsignadas", horasAsignadas);
        resultado.put("horasTotales", horasTotales);
        resultado.put("horasFaltantes", horasFaltantes);

        return ResponseEntity.ok(resultado);
    }

    /**
     * Calcula las horas pedagógicas que representan los bloques seleccionados
     */
    @PostMapping("/calcular-horas-bloques")
    @ResponseBody
    public ResponseEntity<Integer> calcularHorasBloques(@RequestBody List<Long> bloqueIds) {
        // Obtener todos los bloques solicitados
        List<BloqueHorario> bloques = bloqueRepository.findAllById(bloqueIds);

        // Calcular la duración total en minutos
        int totalMinutos = 0;
        for (BloqueHorario bloque : bloques) {
            totalMinutos += bloque.getDuracion();
        }

        // Convertir a horas pedagógicas (45 minutos cada una)
        int horasPedagogicas = totalMinutos / 45;

        return ResponseEntity.ok(horasPedagogicas);
    }
    /**
     * Obtiene cursos por grupo y ciclo con información de estado
     */
    @GetMapping("/cursos/grupo/{grupoId}/ciclo/{cicloId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerCursosPorGrupoYCiclo(
            @PathVariable Long grupoId, @PathVariable Long cicloId) {

        // Buscar el ciclo
        Optional<Ciclo> cicloOpt = cicloRepository.findById(cicloId);
        if (!cicloOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Ciclo ciclo = cicloOpt.get();

        // Obtener todos los cursos del ciclo
        List<Curso> cursos = cursoRepository.findByCiclo(ciclo);
        List<Map<String, Object>> cursosConEstado = new ArrayList<>();

        for (Curso curso : cursos) {
            Map<String, Object> cursoInfo = new HashMap<>();
            cursoInfo.put("idCurso", curso.getIdCurso());
            cursoInfo.put("nombre", curso.getNombre());
            cursoInfo.put("tipo", curso.getTipo());
            cursoInfo.put("horasSemana", curso.getHorasSemana());

            // Calcular horas ya asignadas
            int horasAsignadas = asignacionService.calcularHorasAsignadas(curso.getIdCurso(), grupoId);
            cursoInfo.put("horasAsignadas", horasAsignadas);

            // Determinar el estado
            String estado;
            if (horasAsignadas == 0) {
                estado = "SIN_ASIGNAR";
            } else if (horasAsignadas < curso.getHorasSemana()) {
                estado = "PARCIAL";
            } else {
                estado = "COMPLETO";
            }
            cursoInfo.put("estado", estado);

            cursosConEstado.add(cursoInfo);
        }

        return ResponseEntity.ok(cursosConEstado);
    }
    @GetMapping("/ciclo/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerInfoCiclo(@PathVariable Long id) {
        Optional<Ciclo> cicloOpt = cicloRepository.findById(id);
        if (!cicloOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Ciclo ciclo = cicloOpt.get();
        Carrera carrera = ciclo.getCarrera();
        ModalidadEducativa modalidad = carrera.getModalidad();

        Map<String, Object> info = new HashMap<>();
        info.put("cicloId", ciclo.getIdCiclo());
        info.put("carreraId", carrera.getIdCarrera());
        info.put("modalidadId", modalidad.getIdModalidad());

        return ResponseEntity.ok(info);
    }
}
