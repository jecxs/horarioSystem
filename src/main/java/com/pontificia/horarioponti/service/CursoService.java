package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.CursoDTO;
import com.pontificia.horarioponti.dtos.FiltrosCursoDTO;
import com.pontificia.horarioponti.repository.CarreraRepository;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.CursoRepository;
import com.pontificia.horarioponti.repository.ModalidadEducativaRepository;
import com.pontificia.horarioponti.repository.model.AsignacionHorario;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Curso;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CicloRepository cicloRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private ModalidadEducativaRepository modalidadRepository;

    /**
     * Obtiene todos los cursos
     */
    public List<CursoDTO> obtenerTodosCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un curso por ID
     */
    public CursoDTO obtenerCursoPorId(Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        return cursoOpt.map(this::convertirADTO).orElse(null);
    }

    /**
     * Filtra cursos según criterios
     */
    public List<CursoDTO> filtrarCursos(FiltrosCursoDTO filtros) {
        List<Curso> cursos = new ArrayList<>();

        // Aplicar filtros en orden de especificidad
        if (filtros.getModalidadId() != null) {
            if (filtros.getCarreraId() != null) {
                if (filtros.getCicloId() != null) {
                    // Filtrar por ciclo
                    Optional<Ciclo> cicloOpt = cicloRepository.findById(filtros.getCicloId());
                    if (cicloOpt.isPresent()) {
                        cursos = cursoRepository.findByCiclo(cicloOpt.get());
                    }
                } else {
                    // Filtrar por carrera
                    cursos = cursoRepository.findByCarreraId(filtros.getCarreraId());
                }
            } else {
                // Filtrar por modalidad
                cursos = cursoRepository.findByModalidadId(filtros.getModalidadId());
            }
        } else {
            // Sin filtros específicos, traer todos
            cursos = cursoRepository.findAll();
        }

        // Filtrar por nombre si se especificó
        if (filtros.getNombreCurso() != null && !filtros.getNombreCurso().trim().isEmpty()) {
            cursos = cursos.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(filtros.getNombreCurso().toLowerCase()))
                    .collect(Collectors.toList());
        }

        return cursos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo curso
     */
    @Transactional
    public CursoDTO crearCurso(CursoDTO cursoDTO, Long cicloId) {
        // Verificar que exista el ciclo
        Optional<Ciclo> cicloOpt = cicloRepository.findById(cicloId);
        if (!cicloOpt.isPresent()) {
            return null;
        }

        Ciclo ciclo = cicloOpt.get();

        // Verificar si ya existe un curso con el mismo nombre en el ciclo
        if (cursoRepository.existsByNombreAndCiclo(cursoDTO.getNombre(), ciclo)) {
            return null;
        }

        // Crear el nuevo curso
        Curso curso = new Curso();
        curso.setNombre(cursoDTO.getNombre());
        curso.setTipo(cursoDTO.getTipo());
        curso.setHorasSemana(cursoDTO.getHorasSemana());
        curso.setCiclo(ciclo);
        curso.setAsignaciones(new ArrayList<>());

        // Guardar y convertir
        Curso cursoGuardado = cursoRepository.save(curso);
        return convertirADTO(cursoGuardado);
    }

    /**
     * Actualiza un curso existente
     */
    @Transactional
    public CursoDTO actualizarCurso(Long id, CursoDTO cursoDTO, Long cicloId) {
        // Verificar que existan el curso y el ciclo
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        Optional<Ciclo> cicloOpt = cicloRepository.findById(cicloId);

        if (!cursoOpt.isPresent() || !cicloOpt.isPresent()) {
            return null;
        }

        Curso curso = cursoOpt.get();
        Ciclo ciclo = cicloOpt.get();

        // Verificar si hay otro curso (no este mismo) con el mismo nombre en el ciclo
        boolean existeOtro = cursoRepository.findByCiclo(ciclo).stream()
                .anyMatch(c -> c.getNombre().equals(cursoDTO.getNombre()) && !c.getIdCurso().equals(id));

        if (existeOtro) {
            return null;
        }

        // Actualizar datos
        curso.setNombre(cursoDTO.getNombre());
        curso.setTipo(cursoDTO.getTipo());
        curso.setHorasSemana(cursoDTO.getHorasSemana());
        curso.setCiclo(ciclo);

        // Guardar y convertir
        Curso cursoGuardado = cursoRepository.save(curso);
        return convertirADTO(cursoGuardado);
    }

    /**
     * Elimina un curso si no tiene asignaciones
     */
    @Transactional
    public boolean eliminarCurso(Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            // Verificar si tiene asignaciones
            if (curso.getAsignaciones() == null || curso.getAsignaciones().isEmpty()) {
                cursoRepository.delete(curso);
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte una entidad Curso a DTO
     */
    private CursoDTO convertirADTO(Curso curso) {
        return new CursoDTO(
                curso.getIdCurso(),
                curso.getNombre(),
                curso.getTipo(),
                curso.getHorasSemana(),
                curso.getCiclo().getNumero().toString(),
                curso.getCiclo().getIdCiclo(), // Agregar esto
                curso.getCiclo().getCarrera().getNombre(),
                curso.getCiclo().getCarrera().getModalidad().getNombre()
        );
    }
}