package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.DisponibilidadDocenteDTO;
import com.pontificia.horarioponti.repository.DisponibilidadDocenteRepository;
import com.pontificia.horarioponti.repository.DocenteRepository;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Docente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibilidadDocenteService {

    @Autowired
    private DisponibilidadDocenteRepository disponibilidadRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    /**
     * Obtiene todas las disponibilidades de un docente
     */
    public List<DisponibilidadDocenteDTO> obtenerDisponibilidadesPorDocente(Long docenteId) {
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            return disponibilidadRepository.findByDocente(docente).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Obtiene las disponibilidades de un docente para un día específico
     */
    public List<DisponibilidadDocenteDTO> obtenerDisponibilidadesPorDocenteYDia(
            Long docenteId, DisponibilidadDocente.DiaSemana diaSemana) {
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            return disponibilidadRepository.findByDocenteAndDiaSemana(docente, diaSemana).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Crea una nueva disponibilidad para un docente
     */
    @Transactional
    public DisponibilidadDocenteDTO crearDisponibilidad(
            Long docenteId, DisponibilidadDocente.DiaSemana diaSemana,
            LocalTime horaInicio, LocalTime horaFin) {

        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (!docenteOpt.isPresent()) {
            return null;
        }

        Docente docente = docenteOpt.get();

        // Verificar que la hora fin sea después de la hora inicio
        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            return null;
        }

        // Verificar si ya existe una disponibilidad que se solape
        List<DisponibilidadDocente> disponibilidades = disponibilidadRepository
                .findByDocenteAndDiaSemana(docente, diaSemana);

        boolean existeSolapamiento = disponibilidades.stream()
                .anyMatch(d ->
                        (horaInicio.isBefore(d.getHoraFin()) && horaFin.isAfter(d.getHoraInicio())) ||
                                horaInicio.equals(d.getHoraInicio()) ||
                                horaFin.equals(d.getHoraFin())
                );

        if (existeSolapamiento) {
            return null;
        }

        // Crear disponibilidad
        DisponibilidadDocente disponibilidad = new DisponibilidadDocente();
        disponibilidad.setDocente(docente);
        disponibilidad.setDiaSemana(diaSemana);
        disponibilidad.setHoraInicio(horaInicio);
        disponibilidad.setHoraFin(horaFin);

        DisponibilidadDocente disponibilidadGuardada = disponibilidadRepository.save(disponibilidad);
        return convertirADTO(disponibilidadGuardada);
    }

    /**
     * Elimina una disponibilidad
     */
    @Transactional
    public boolean eliminarDisponibilidad(Long id) {
        Optional<DisponibilidadDocente> disponibilidadOpt = disponibilidadRepository.findById(id);
        if (disponibilidadOpt.isPresent()) {
            DisponibilidadDocente disponibilidad = disponibilidadOpt.get();

            // Verificar si hay asignaciones que dependan de esta disponibilidad
            Docente docente = disponibilidad.getDocente();
            boolean hayAsignacionesEnHorario = docente.getAsignaciones().stream()
                    .anyMatch(a ->
                            a.getDiaSemana() == disponibilidad.getDiaSemana() &&
                                    a.getBloques().stream().anyMatch(b ->
                                            (b.getHoraInicio().isAfter(disponibilidad.getHoraInicio()) ||
                                                    b.getHoraInicio().equals(disponibilidad.getHoraInicio())) &&
                                                    (b.getHoraFin().isBefore(disponibilidad.getHoraFin()) ||
                                                            b.getHoraFin().equals(disponibilidad.getHoraFin()))
                                    )
                    );

            if (!hayAsignacionesEnHorario) {
                disponibilidadRepository.delete(disponibilidad);
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte una entidad DisponibilidadDocente a DTO
     */
    private DisponibilidadDocenteDTO convertirADTO(DisponibilidadDocente disponibilidad) {
        return new DisponibilidadDocenteDTO(
                disponibilidad.getId(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin()
        );
    }
}
