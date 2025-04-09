package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.repository.BloqueHorarioRepository;
import com.pontificia.horarioponti.repository.TurnoRepository;
import com.pontificia.horarioponti.repository.model.BloqueHorario;
import com.pontificia.horarioponti.repository.model.Turno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    /**
     * Obtiene todos los turnos
     */
    public List<Turno> obtenerTodosTurnos() {
        return turnoRepository.findAll();
    }

    /**
     * Obtiene un turno por ID
     */
    public Turno obtenerTurnoPorId(Long id) {
        return turnoRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene un turno por nombre
     */
    public Turno obtenerTurnoPorNombre(String nombre) {
        return turnoRepository.findByNombre(nombre).orElse(null);
    }

    /**
     * Obtiene el turno que contiene una hora específica
     */
    public Turno obtenerTurnoPorHora(LocalTime hora) {
        return turnoRepository.findTurnoContainingHora(hora).orElse(null);
    }

    /**
     * Crea un nuevo turno
     */
    @Transactional
    public Turno crearTurno(String nombre, LocalTime horaInicio, LocalTime horaFin) {
        if (turnoRepository.existsByNombre(nombre)) {
            return null;
        }

        // Verificar que la hora fin sea después de la hora inicio
        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            return null;
        }

        Turno turno = new Turno();
        turno.setNombre(nombre);
        turno.setHoraInicio(horaInicio);
        turno.setHoraFin(horaFin);
        turno.setBloques(new ArrayList<>());

        return turnoRepository.save(turno);
    }

    /**
     * Actualiza un turno existente
     */
    @Transactional
    public Turno actualizarTurno(Long id, String nombre, LocalTime horaInicio, LocalTime horaFin) {
        Optional<Turno> turnoOpt = turnoRepository.findById(id);
        if (!turnoOpt.isPresent()) {
            return null;
        }

        Turno turno = turnoOpt.get();

        // Verificar que no exista otro turno (no este mismo) con el mismo nombre
        boolean existeOtro = turnoRepository.findAll().stream()
                .anyMatch(t -> t.getNombre().equals(nombre) && !t.getIdTurno().equals(id));

        if (existeOtro) {
            return null;
        }

        // Verificar que la hora fin sea después de la hora inicio
        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            return null;
        }

        // Verificar que no tenga bloques con asignaciones
        boolean tieneBloques = turno.getBloques() != null && !turno.getBloques().isEmpty();
        boolean hayAsignaciones = tieneBloques && turno.getBloques().stream()
                .anyMatch(b -> b.getAsignaciones() != null && !b.getAsignaciones().isEmpty());

        if (hayAsignaciones) {
            return null;
        }

        // Actualizar datos
        turno.setNombre(nombre);
        turno.setHoraInicio(horaInicio);
        turno.setHoraFin(horaFin);

        return turnoRepository.save(turno);
    }

    /**
     * Elimina un turno si no tiene bloques con asignaciones
     */
    @Transactional
    public boolean eliminarTurno(Long id) {
        Optional<Turno> turnoOpt = turnoRepository.findById(id);
        if (turnoOpt.isPresent()) {
            Turno turno = turnoOpt.get();

            // Verificar que no tenga bloques con asignaciones
            boolean tieneBloques = turno.getBloques() != null && !turno.getBloques().isEmpty();
            boolean hayAsignaciones = tieneBloques && turno.getBloques().stream()
                    .anyMatch(b -> b.getAsignaciones() != null && !b.getAsignaciones().isEmpty());

            if (!hayAsignaciones) {
                turnoRepository.delete(turno);
                return true;
            }
        }
        return false;
    }
}