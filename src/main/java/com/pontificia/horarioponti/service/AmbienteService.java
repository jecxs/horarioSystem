package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.AmbienteDTO;
import com.pontificia.horarioponti.dtos.BloqueDisponibleDTO;
import com.pontificia.horarioponti.repository.AmbienteRepository;
import com.pontificia.horarioponti.repository.AsignacionHorarioRepository;
import com.pontificia.horarioponti.repository.BloqueHorarioRepository;
import com.pontificia.horarioponti.repository.model.Ambiente;
import com.pontificia.horarioponti.repository.model.BloqueHorario;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private AsignacionHorarioRepository asignacionRepository;

    /**
     * Obtiene todos los ambientes
     */
    public List<AmbienteDTO> obtenerTodosAmbientes() {
        return ambienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene ambientes por tipo
     */
    public List<AmbienteDTO> obtenerAmbientesPorTipo(Ambiente.TipoAmbiente tipo) {
        return ambienteRepository.findByTipo(tipo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene ambientes con capacidad mínima
     */
    public List<AmbienteDTO> obtenerAmbientesPorCapacidadMinima(Integer capacidadMinima) {
        return ambienteRepository.findByCapacidadGreaterThanEqual(capacidadMinima).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene ambientes por tipo y capacidad mínima
     */
    public List<AmbienteDTO> obtenerAmbientesPorTipoYCapacidadMinima(Ambiente.TipoAmbiente tipo, Integer capacidadMinima) {
        return ambienteRepository.findByTipoAndCapacidadMinima(tipo, capacidadMinima).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un ambiente por ID
     */
    public AmbienteDTO obtenerAmbientePorId(Long id) {
        Optional<Ambiente> ambienteOpt = ambienteRepository.findById(id);
        return ambienteOpt.map(this::convertirADTO).orElse(null);
    }

    /**
     * Crea un nuevo ambiente
     */
    @Transactional
    public AmbienteDTO crearAmbiente(AmbienteDTO ambienteDTO) {
        // Verificar que no exista otro ambiente con el mismo nombre
        if (ambienteRepository.existsByNombre(ambienteDTO.getNombre())) {
            return null;
        }

        // Crear el ambiente
        Ambiente ambiente = new Ambiente();
        ambiente.setNombre(ambienteDTO.getNombre());
        ambiente.setTipo(ambienteDTO.getTipo());
        ambiente.setCapacidad(ambienteDTO.getCapacidad());

        // Guardar y convertir
        Ambiente ambienteGuardado = ambienteRepository.save(ambiente);
        return convertirADTO(ambienteGuardado);
    }

    /**
     * Actualiza un ambiente existente
     */
    @Transactional
    public AmbienteDTO actualizarAmbiente(Long id, AmbienteDTO ambienteDTO) {
        // Verificar que exista el ambiente
        Optional<Ambiente> ambienteOpt = ambienteRepository.findById(id);
        if (!ambienteOpt.isPresent()) {
            return null;
        }

        Ambiente ambiente = ambienteOpt.get();

        // Verificar que no exista otro ambiente (no este mismo) con el mismo nombre
        boolean existeOtro = ambienteRepository.findAll().stream()
                .anyMatch(a -> a.getNombre().equals(ambienteDTO.getNombre()) && !a.getIdAmbiente().equals(id));

        if (existeOtro) {
            return null;
        }

        // Actualizar datos
        ambiente.setNombre(ambienteDTO.getNombre());
        ambiente.setTipo(ambienteDTO.getTipo());
        ambiente.setCapacidad(ambienteDTO.getCapacidad());

        // Guardar y convertir
        Ambiente ambienteGuardado = ambienteRepository.save(ambiente);
        return convertirADTO(ambienteGuardado);
    }

    /**
     * Elimina un ambiente si no tiene asignaciones
     */
    @Transactional
    public boolean eliminarAmbiente(Long id) {
        Optional<Ambiente> ambienteOpt = ambienteRepository.findById(id);
        if (ambienteOpt.isPresent()) {
            Ambiente ambiente = ambienteOpt.get();
            // Verificar si tiene asignaciones
            if (ambiente.getAsignaciones() == null || ambiente.getAsignaciones().isEmpty()) {
                ambienteRepository.delete(ambiente);
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte una entidad Ambiente a DTO
     */
    private AmbienteDTO convertirADTO(Ambiente ambiente) {
        return new AmbienteDTO(
                ambiente.getIdAmbiente(),
                ambiente.getNombre(),
                ambiente.getTipo(),
                ambiente.getCapacidad()
        );
    }
}
