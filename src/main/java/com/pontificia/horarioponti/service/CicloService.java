package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.repository.CarreraRepository;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.GrupoRepository;
import com.pontificia.horarioponti.repository.model.Carrera;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Grupo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CicloService {

    private final CicloRepository cicloRepository;
    private final CarreraRepository carreraRepository;
    private final GrupoRepository grupoRepository;

    public List<Ciclo> getAllCiclos() {
        return cicloRepository.findAll();
    }

    public Ciclo getCicloById(Long id) {
        return cicloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + id));
    }

    public List<Ciclo> getCiclosByCarrera(Long carreraId) {
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + carreraId));

        return cicloRepository.findByCarrera(carrera);
    }

    @Transactional
    public Ciclo createCiclo(Ciclo ciclo) {
        // Validar que exista la carrera
        Carrera carrera = carreraRepository.findById(ciclo.getCarrera().getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: "
                        + ciclo.getCarrera().getIdCarrera()));

        // Validar que no exista un ciclo con el mismo número en la misma carrera
        if (cicloRepository.existsByNumeroAndCarrera(ciclo.getNumero(), carrera)) {
            throw new RuntimeException("Ya existe un ciclo con el número " + ciclo.getNumero()
                    + " en la carrera " + carrera.getNombre());
        }

        ciclo.setCarrera(carrera);
        return cicloRepository.save(ciclo);
    }

    @Transactional
    public Ciclo updateCiclo(Long id, Ciclo cicloDetails) {
        Ciclo ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + id));

        // Validar que exista la carrera
        Carrera carrera = carreraRepository.findById(cicloDetails.getCarrera().getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: "
                        + cicloDetails.getCarrera().getIdCarrera()));

        // Validar que no exista otro ciclo con el mismo número en la misma carrera
        if (!ciclo.getNumero().equals(cicloDetails.getNumero()) &&
                cicloRepository.existsByNumeroAndCarrera(cicloDetails.getNumero(), carrera)) {
            throw new RuntimeException("Ya existe un ciclo con el número " + cicloDetails.getNumero()
                    + " en la carrera " + carrera.getNombre());
        }

        ciclo.setNumero(cicloDetails.getNumero());
        ciclo.setCarrera(carrera);

        return cicloRepository.save(ciclo);
    }

    @Transactional
    public void deleteCiclo(Long id) {
        Ciclo ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + id));

        // Verificar si tiene cursos o grupos asociados
        if ((ciclo.getCursos() != null && !ciclo.getCursos().isEmpty()) ||
                (ciclo.getGrupos() != null && !ciclo.getGrupos().isEmpty())) {
            throw new RuntimeException("No se puede eliminar el ciclo porque tiene cursos o grupos asociados");
        }

        cicloRepository.delete(ciclo);
    }

    // Método para crear un grupo para un ciclo específico
    @Transactional
    public Grupo crearGrupoParaCiclo(Long cicloId, String nombreGrupo) {
        Ciclo ciclo = cicloRepository.findById(cicloId)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + cicloId));

        // Validar que no exista un grupo con el mismo nombre en el mismo ciclo
        if (grupoRepository.existsByNombreAndCiclo(nombreGrupo, ciclo)) {
            throw new RuntimeException("Ya existe un grupo con el nombre " + nombreGrupo +
                    " en el ciclo " + ciclo.getNumero());
        }

        Grupo grupo = new Grupo();
        grupo.setNombre(nombreGrupo);
        grupo.setCiclo(ciclo);

        return grupoRepository.save(grupo);
    }

    // Método para crear grupos automáticamente (A, B) para un ciclo
    @Transactional
    public List<Grupo> crearGruposAutomaticosParaCiclo(Long cicloId) {
        Ciclo ciclo = cicloRepository.findById(cicloId)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + cicloId));

        List<Grupo> gruposCreados = new ArrayList<>();

        // Crear grupo A
        if (!grupoRepository.existsByNombreAndCiclo("A", ciclo)) {
            Grupo grupoA = new Grupo();
            grupoA.setNombre("A");
            grupoA.setCiclo(ciclo);
            gruposCreados.add(grupoRepository.save(grupoA));
        }

        // Crear grupo B
        if (!grupoRepository.existsByNombreAndCiclo("B", ciclo)) {
            Grupo grupoB = new Grupo();
            grupoB.setNombre("B");
            grupoB.setCiclo(ciclo);
            gruposCreados.add(grupoRepository.save(grupoB));
        }

        return gruposCreados;
    }

    // Método para obtener información del ciclo incluyendo sus cursos y grupos
    public Map<String, Object> getInfoCompletaCiclo(Long cicloId) {
        Ciclo ciclo = cicloRepository.findById(cicloId)
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + cicloId));

        Map<String, Object> infoCiclo = new HashMap<>();
        infoCiclo.put("idCiclo", ciclo.getIdCiclo());
        infoCiclo.put("numero", ciclo.getNumero());
        infoCiclo.put("carrera", ciclo.getCarrera().getNombre());
        infoCiclo.put("modalidad", ciclo.getCarrera().getModalidad().getNombre());

        // Obtener cursos del ciclo
        if (ciclo.getCursos() != null) {
            List<Map<String, Object>> cursos = ciclo.getCursos().stream()
                    .map(curso -> {
                        Map<String, Object> cursoInfo = new HashMap<>();
                        cursoInfo.put("idCurso", curso.getIdCurso());
                        cursoInfo.put("nombre", curso.getNombre());
                        cursoInfo.put("tipo", curso.getTipo());
                        cursoInfo.put("horasSemana", curso.getHorasSemana());
                        return cursoInfo;
                    })
                    .collect(Collectors.toList());
            infoCiclo.put("cursos", cursos);
        }

        // Obtener grupos del ciclo
        if (ciclo.getGrupos() != null) {
            List<Map<String, Object>> grupos = ciclo.getGrupos().stream()
                    .map(grupo -> {
                        Map<String, Object> grupoInfo = new HashMap<>();
                        grupoInfo.put("idGrupo", grupo.getIdGrupo());
                        grupoInfo.put("nombre", grupo.getNombre());
                        return grupoInfo;
                    })
                    .collect(Collectors.toList());
            infoCiclo.put("grupos", grupos);
        }

        return infoCiclo;
    }
}