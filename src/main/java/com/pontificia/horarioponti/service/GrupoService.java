package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.GrupoDTO;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.GrupoRepository;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Grupo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CicloRepository cicloRepository;

    /**
     * Obtiene todos los grupos
     */
    public List<GrupoDTO> obtenerTodosGrupos() {
        return grupoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene grupos por ciclo
     */
    public List<GrupoDTO> obtenerGruposPorCiclo(Long cicloId) {
        Optional<Ciclo> cicloOpt = cicloRepository.findById(cicloId);
        if (cicloOpt.isPresent()) {
            Ciclo ciclo = cicloOpt.get();
            return grupoRepository.findByCiclo(ciclo).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Obtiene un grupo por ID
     */
    public GrupoDTO obtenerGrupoPorId(Long id) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        return grupoOpt.map(this::convertirADTO).orElse(null);
    }

    /**
     * Crea un nuevo grupo para un ciclo
     * Esta operación es clave ya que habilita los cursos para asignación
     */
    @Transactional
    public GrupoDTO crearGrupo(String nombre, Long cicloId) {
        // Verificar que exista el ciclo
        Optional<Ciclo> cicloOpt = cicloRepository.findById(cicloId);
        if (!cicloOpt.isPresent()) {
            return null;
        }

        Ciclo ciclo = cicloOpt.get();

        // Verificar que no exista otro grupo con el mismo nombre en el ciclo
        if (grupoRepository.existsByNombreAndCiclo(nombre, ciclo)) {
            return null;
        }

        // Crear el grupo
        Grupo grupo = new Grupo();
        grupo.setNombre(nombre);
        grupo.setCiclo(ciclo);

        // Guardar y convertir
        Grupo grupoGuardado = grupoRepository.save(grupo);
        return convertirADTO(grupoGuardado);
    }

    /**
     * Elimina un grupo si no tiene asignaciones
     */
    @Transactional
    public boolean eliminarGrupo(Long id) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            // Verificar si tiene asignaciones
            if (grupo.getAsignaciones() == null || grupo.getAsignaciones().isEmpty()) {
                grupoRepository.delete(grupo);
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte una entidad Grupo a DTO
     */
    private GrupoDTO convertirADTO(Grupo grupo) {
        return new GrupoDTO(
                grupo.getIdGrupo(),
                grupo.getNombre(),
                grupo.getCiclo().getIdCiclo(),
                "Ciclo " + grupo.getCiclo().getNumero()
        );
    }
}
