package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.*;
import com.pontificia.horarioponti.repository.AsignacionHorarioRepository;
import com.pontificia.horarioponti.repository.BloqueHorarioRepository;
import com.pontificia.horarioponti.repository.DisponibilidadDocenteRepository;
import com.pontificia.horarioponti.repository.DocenteRepository;
import com.pontificia.horarioponti.repository.model.AsignacionHorario;
import com.pontificia.horarioponti.repository.model.BloqueHorario;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Docente;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DisponibilidadDocenteRepository disponibilidadRepository;

    @Autowired
    private AsignacionHorarioRepository asignacionRepository;

    /**
     * Obtiene todos los docentes con sus disponibilidades
     */
    public List<DocenteDTO> obtenerTodosDocentes() {
        return docenteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca docentes por nombre
     */
    public List<DocenteDTO> buscarDocentesPorNombre(String nombre) {
        return docenteRepository.findByNombreCompletoContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un docente por ID con sus disponibilidades
     */
    public DocenteDTO obtenerDocentePorId(Long id) {
        Optional<Docente> docenteOpt = docenteRepository.findById(id);
        return docenteOpt.map(this::convertirADTO).orElse(null);
    }

    /**
     * Crea un nuevo docente con sus disponibilidades
     */
    @Transactional
    public DocenteDTO crearDocente(DocenteDTO docenteDTO) {
        Docente docente = new Docente();
        docente.setNombreCompleto(docenteDTO.getNombreCompleto());
        docente.setEspecialidad(docenteDTO.getEspecialidad());
        docente.setDisponibilidades(new ArrayList<>());

        // Guardar primero el docente
        Docente docenteGuardado = docenteRepository.save(docente);

        // Procesar disponibilidades si existen
        if (docenteDTO.getDisponibilidades() != null && !docenteDTO.getDisponibilidades().isEmpty()) {
            List<DisponibilidadDocente> disponibilidades = docenteDTO.getDisponibilidades().stream()
                    .map(dispDTO -> {
                        DisponibilidadDocente disponibilidad = new DisponibilidadDocente();
                        disponibilidad.setDocente(docenteGuardado);
                        disponibilidad.setDiaSemana(dispDTO.getDiaSemana());
                        disponibilidad.setHoraInicio(dispDTO.getHoraInicio());
                        disponibilidad.setHoraFin(dispDTO.getHoraFin());
                        return disponibilidad;
                    })
                    .collect(Collectors.toList());

            disponibilidadRepository.saveAll(disponibilidades);
            docenteGuardado.setDisponibilidades(disponibilidades);
        }

        return convertirADTO(docenteGuardado);
    }

    /**
     * Actualiza un docente existente
     */
    @Transactional
    public DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO) {
        Optional<Docente> docenteOpt = docenteRepository.findById(id);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            docente.setNombreCompleto(docenteDTO.getNombreCompleto());
            docente.setEspecialidad(docenteDTO.getEspecialidad());

            // Eliminar disponibilidades anteriores
            disponibilidadRepository.deleteByDocente(docente);

            // Agregar nuevas disponibilidades
            if (docenteDTO.getDisponibilidades() != null && !docenteDTO.getDisponibilidades().isEmpty()) {
                List<DisponibilidadDocente> disponibilidades = docenteDTO.getDisponibilidades().stream()
                        .map(dispDTO -> {
                            DisponibilidadDocente disponibilidad = new DisponibilidadDocente();
                            disponibilidad.setDocente(docente);
                            disponibilidad.setDiaSemana(dispDTO.getDiaSemana());
                            disponibilidad.setHoraInicio(dispDTO.getHoraInicio());
                            disponibilidad.setHoraFin(dispDTO.getHoraFin());
                            return disponibilidad;
                        })
                        .collect(Collectors.toList());

                disponibilidadRepository.saveAll(disponibilidades);
                docente.setDisponibilidades(disponibilidades);
            } else {
                docente.setDisponibilidades(new ArrayList<>());
            }

            return convertirADTO(docenteRepository.save(docente));
        }
        return null;
    }

    /**
     * Elimina un docente si no tiene asignaciones
     */
    @Transactional
    public boolean eliminarDocente(Long id) {
        Optional<Docente> docenteOpt = docenteRepository.findById(id);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            // Verificar si tiene asignaciones
            if (docente.getAsignaciones() == null || docente.getAsignaciones().isEmpty()) {
                disponibilidadRepository.deleteByDocente(docente);
                docenteRepository.delete(docente);
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte una entidad Docente a DTO
     */
    private DocenteDTO convertirADTO(Docente docente) {
        DocenteDTO dto = new DocenteDTO();
        dto.setIdDocente(docente.getIdDocente());
        dto.setNombreCompleto(docente.getNombreCompleto());
        dto.setEspecialidad(docente.getEspecialidad());

        if (docente.getDisponibilidades() != null) {
            List<DisponibilidadDocenteDTO> disponibilidadesDTO = docente.getDisponibilidades().stream()
                    .map(this::convertirDisponibilidadADTO)
                    .collect(Collectors.toList());
            dto.setDisponibilidades(disponibilidadesDTO);
        } else {
            dto.setDisponibilidades(new ArrayList<>());
        }

        return dto;
    }

    /**
     * Convierte una entidad DisponibilidadDocente a DTO
     */
    private DisponibilidadDocenteDTO convertirDisponibilidadADTO(DisponibilidadDocente disponibilidad) {
        DisponibilidadDocenteDTO dto = new DisponibilidadDocenteDTO();
        dto.setId(disponibilidad.getId());
        dto.setDiaSemana(disponibilidad.getDiaSemana());
        dto.setHoraInicio(disponibilidad.getHoraInicio());
        dto.setHoraFin(disponibilidad.getHoraFin());
        return dto;
    }
}