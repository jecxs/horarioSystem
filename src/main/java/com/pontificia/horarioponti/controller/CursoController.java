package com.pontificia.horarioponti.controller;

import com.pontificia.horarioponti.dtos.CursoDTO;
import com.pontificia.horarioponti.dtos.GrupoDTO;
import com.pontificia.horarioponti.repository.model.Carrera;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import com.pontificia.horarioponti.service.CarreraService;
import com.pontificia.horarioponti.service.CursoService;
import com.pontificia.horarioponti.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private CarreraService carreraService;

    /**
     * Obtiene todos los cursos
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CursoDTO>> obtenerTodosCursos() {
        return ResponseEntity.ok(cursoService.obtenerTodosCursos());
    }

    /**
     * Obtiene un curso por ID
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@PathVariable Long id) {
        CursoDTO curso = cursoService.obtenerCursoPorId(id);
        if (curso != null) {
            return ResponseEntity.ok(curso);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea un nuevo curso
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<CursoDTO> crearCurso(
            @RequestBody CursoDTO cursoDTO,
            @RequestParam Long cicloId) {

        CursoDTO nuevoCurso = cursoService.crearCurso(cursoDTO, cicloId);
        if (nuevoCurso != null) {
            return ResponseEntity.ok(nuevoCurso);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Actualiza un curso existente
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CursoDTO> actualizarCurso(
            @PathVariable Long id,
            @RequestBody CursoDTO cursoDTO,
            @RequestParam Long cicloId) {

        CursoDTO cursoActualizado = cursoService.actualizarCurso(id, cursoDTO, cicloId);
        if (cursoActualizado != null) {
            return ResponseEntity.ok(cursoActualizado);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina un curso
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        boolean eliminado = cursoService.eliminarCurso(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todos los grupos
     */
    @GetMapping("/grupos")
    @ResponseBody
    public ResponseEntity<List<GrupoDTO>> obtenerTodosGrupos() {
        return ResponseEntity.ok(grupoService.obtenerTodosGrupos());
    }

    /**
     * Crea un nuevo grupo
     */
    @PostMapping("/grupos")
    @ResponseBody
    public ResponseEntity<GrupoDTO> crearGrupo(
            @RequestParam String nombre,
            @RequestParam Long cicloId) {

        GrupoDTO nuevoGrupo = grupoService.crearGrupo(nombre, cicloId);
        if (nuevoGrupo != null) {
            return ResponseEntity.ok(nuevoGrupo);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Elimina un grupo
     */
    @DeleteMapping("/grupos/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarGrupo(@PathVariable Long id) {
        boolean eliminado = grupoService.eliminarGrupo(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todas las modalidades educativas
     */
    @GetMapping("/modalidades")
    @ResponseBody
    public ResponseEntity<List<ModalidadEducativa>> obtenerModalidades() {
        return ResponseEntity.ok(carreraService.obtenerTodasModalidades());
    }

    /**
     * Crea una nueva modalidad educativa
     */
    @PostMapping("/modalidades")
    @ResponseBody
    public ResponseEntity<ModalidadEducativa> crearModalidad(
            @RequestParam String nombre,
            @RequestParam Integer duracionAnios) {

        ModalidadEducativa nuevaModalidad = carreraService.crearModalidad(nombre, duracionAnios);
        if (nuevaModalidad != null) {
            return ResponseEntity.ok(nuevaModalidad);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todas las carreras
     */
    @GetMapping("/carreras")
    @ResponseBody
    public ResponseEntity<List<Carrera>> obtenerCarreras() {
        return ResponseEntity.ok(carreraService.obtenerTodasCarreras());
    }

    /**
     * Obtiene carreras por modalidad
     */
    @GetMapping("/carreras/modalidad/{modalidadId}")
    @ResponseBody
    public ResponseEntity<List<Carrera>> obtenerCarrerasPorModalidad(@PathVariable Long modalidadId) {
        return ResponseEntity.ok(carreraService.obtenerCarrerasPorModalidad(modalidadId));
    }

    /**
     * Crea una nueva carrera
     */
    @PostMapping("/carreras")
    @ResponseBody
    public ResponseEntity<Carrera> crearCarrera(
            @RequestParam String nombre,
            @RequestParam Long modalidadId) {

        Carrera nuevaCarrera = carreraService.crearCarrera(nombre, modalidadId);
        if (nuevaCarrera != null) {
            return ResponseEntity.ok(nuevaCarrera);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene todos los ciclos
     */
    @GetMapping("/ciclos")
    @ResponseBody
    public ResponseEntity<List<Ciclo>> obtenerCiclos() {
        return ResponseEntity.ok(carreraService.obtenerTodosCiclos());
    }

    /**
     * Obtiene ciclos por carrera
     */
    @GetMapping("/ciclos/carrera/{carreraId}")
    @ResponseBody
    public ResponseEntity<List<Ciclo>> obtenerCiclosPorCarrera(@PathVariable Long carreraId) {
        return ResponseEntity.ok(carreraService.obtenerCiclosPorCarrera(carreraId));
    }

    /**
     * Crea un nuevo ciclo
     */
    @PostMapping("/ciclos")
    @ResponseBody
    public ResponseEntity<Ciclo> crearCiclo(
            @RequestParam Integer numero,
            @RequestParam Long carreraId) {

        Ciclo nuevoCiclo = carreraService.crearCiclo(numero, carreraId);
        if (nuevoCiclo != null) {
            return ResponseEntity.ok(nuevoCiclo);
        }
        return ResponseEntity.badRequest().build();
    }
    /**
     * Filtra grupos según criterios
     */
    @GetMapping("/grupos/filtrar")
    @ResponseBody
    public ResponseEntity<List<GrupoDTO>> filtrarGrupos(
            @RequestParam(required = false) Long modalidadId,
            @RequestParam(required = false) Long carreraId) {

        List<GrupoDTO> grupos = new ArrayList<>();

        if (carreraId != null) {
            // Si tenemos carreraId, filtramos por ciclos de esa carrera
            List<Ciclo> ciclos = carreraService.obtenerCiclosPorCarrera(carreraId);
            for (Ciclo ciclo : ciclos) {
                grupos.addAll(grupoService.obtenerGruposPorCiclo(ciclo.getIdCiclo()));
            }
        } else if (modalidadId != null) {
            // Si solo tenemos modalidadId, filtramos por carreras de esa modalidad
            List<Carrera> carreras = carreraService.obtenerCarrerasPorModalidad(modalidadId);
            for (Carrera carrera : carreras) {
                List<Ciclo> ciclos = carreraService.obtenerCiclosPorCarrera(carrera.getIdCarrera());
                for (Ciclo ciclo : ciclos) {
                    grupos.addAll(grupoService.obtenerGruposPorCiclo(ciclo.getIdCiclo()));
                }
            }
        } else {
            // Sin filtros, traer todos los grupos
            grupos = grupoService.obtenerTodosGrupos();
        }

        return ResponseEntity.ok(grupos);
    }
}
