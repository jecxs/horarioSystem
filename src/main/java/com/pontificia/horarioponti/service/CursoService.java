package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.CursoDTO;
import com.pontificia.horarioponti.dtos.FiltrosCursoDTO;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.CursoRepository;
import com.pontificia.horarioponti.repository.model.AsignacionHorario;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Curso;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CicloRepository cicloRepository;

    public List<CursoDTO> getAllCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CursoDTO getCursoById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

        return convertToDTO(curso);
    }

    @Transactional
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        // Validar que exista el ciclo
        Ciclo ciclo = obtenerCicloPorId(cursoDTO.getCicloNombre());

        // Validar que no exista un curso con el mismo nombre en el mismo ciclo
        if (cursoRepository.existsByNombreAndCiclo(cursoDTO.getNombre(), ciclo)) {
            throw new RuntimeException("Ya existe un curso con el nombre " + cursoDTO.getNombre() + " en el ciclo " + ciclo.getNumero());
        }

        Curso curso = new Curso();
        curso.setNombre(cursoDTO.getNombre());
        curso.setTipo(cursoDTO.getTipo());
        curso.setHorasSemana(cursoDTO.getHorasSemana());
        curso.setCiclo(ciclo);

        Curso savedCurso = cursoRepository.save(curso);

        return convertToDTO(savedCurso);
    }

    @Transactional
    public CursoDTO updateCurso(Long id, CursoDTO cursoDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

        // Validar que exista el ciclo
        Ciclo ciclo = obtenerCicloPorId(cursoDTO.getCicloNombre());

        // Validar que no exista otro curso con el mismo nombre en el mismo ciclo
        if (!curso.getNombre().equals(cursoDTO.getNombre()) &&
                cursoRepository.existsByNombreAndCiclo(cursoDTO.getNombre(), ciclo)) {
            throw new RuntimeException("Ya existe un curso con el nombre " + cursoDTO.getNombre() + " en el ciclo " + ciclo.getNumero());
        }

        curso.setNombre(cursoDTO.getNombre());
        curso.setTipo(cursoDTO.getTipo());
        curso.setHorasSemana(cursoDTO.getHorasSemana());
        curso.setCiclo(ciclo);

        Curso updatedCurso = cursoRepository.save(curso);

        return convertToDTO(updatedCurso);
    }

    @Transactional
    public void deleteCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

        // Verificar si tiene asignaciones
        if (curso.getAsignaciones() != null && !curso.getAsignaciones().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el curso porque tiene asignaciones de horario");
        }

        cursoRepository.delete(curso);
    }

    public List<CursoDTO> getCursosByCarrera(Long carreraId) {
        return cursoRepository.findByCarreraId(carreraId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> getCursosByModalidad(Long modalidadId) {
        return cursoRepository.findByModalidadId(modalidadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> getCursosByCiclo(Long cicloId) {
        Ciclo ciclo = cicloRepository.findById(cicloId)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + cicloId));

        return cursoRepository.findByCiclo(ciclo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> getCursosByNombre(String nombre) {
        return cursoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> getCursosFiltrados(FiltrosCursoDTO filtros) {
        List<Curso> cursos;

        if (filtros.getCicloId() != null) {
            Ciclo ciclo = cicloRepository.findById(filtros.getCicloId())
                    .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + filtros.getCicloId()));
            cursos = cursoRepository.findByCiclo(ciclo);
        } else if (filtros.getCarreraId() != null) {
            cursos = cursoRepository.findByCarreraId(filtros.getCarreraId());
        } else if (filtros.getModalidadId() != null) {
            cursos = cursoRepository.findByModalidadId(filtros.getModalidadId());
        } else if (filtros.getNombreCurso() != null && !filtros.getNombreCurso().isEmpty()) {
            cursos = cursoRepository.findByNombreContainingIgnoreCase(filtros.getNombreCurso());
        } else {
            cursos = cursoRepository.findAll();
        }

        return cursos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener la cantidad total de horas asignadas a un curso
    public int getHorasAsignadas(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cursoId));

        int horasAsignadas = 0;
        if (curso.getAsignaciones() != null) {
            for (AsignacionHorario asignacion : curso.getAsignaciones()) {
                // Cada bloque horario es típicamente de 45 minutos (o lo que se haya configurado)
                horasAsignadas += asignacion.getBloques().size();
            }
        }

        // Convertir bloques a horas (cada bloque es de 45 minutos = 0.75 horas)
        return (int) Math.ceil(horasAsignadas * 0.75);
    }

    // Método para verificar si ya se han asignado todas las horas semanales requeridas
    public boolean verificarHorasCompletadas(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cursoId));

        int horasAsignadas = getHorasAsignadas(cursoId);
        return horasAsignadas >= curso.getHorasSemana();
    }

    private Ciclo obtenerCicloPorId(String cicloNombre) {
        try {
            Long cicloId = Long.parseLong(cicloNombre);
            return cicloRepository.findById(cicloId)
                    .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + cicloId));
        } catch (NumberFormatException e) {
            // Si no es un ID, intentar extraer el número del formato "Ciclo X"
            if (cicloNombre != null && cicloNombre.startsWith("Ciclo ")) {
                try {
                    String numeroStr = cicloNombre.substring(6).trim();
                    Integer numero = Integer.parseInt(numeroStr);
                    // Aquí, idealmente necesitaríamos la carrera también, pero como es solo referencia interna,
                    // podríamos buscar el primero que coincida con ese número
                    List<Ciclo> ciclos = cicloRepository.findAll().stream()
                            .filter(c -> c.getNumero().equals(numero))
                            .collect(Collectors.toList());

                    if (!ciclos.isEmpty()) {
                        return ciclos.get(0);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Formato de ciclo inválido: " + cicloNombre);
                }
            }
            throw new RuntimeException("Ciclo no encontrado: " + cicloNombre);
        }
    }

    private CursoDTO convertToDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(curso.getIdCurso());
        dto.setNombre(curso.getNombre());
        dto.setTipo(curso.getTipo());
        dto.setHorasSemana(curso.getHorasSemana());

        // Información del ciclo
        dto.setCicloNombre("Ciclo " + curso.getCiclo().getNumero());
        dto.setCarreraNombre(curso.getCiclo().getCarrera().getNombre());
        dto.setModalidadNombre(curso.getCiclo().getCarrera().getModalidad().getNombre());

        return dto;
    }
}
