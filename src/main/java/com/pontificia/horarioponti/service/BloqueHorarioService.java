package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.BloqueHorarioDTO;
import com.pontificia.horarioponti.repository.BloqueHorarioRepository;
import com.pontificia.horarioponti.repository.TurnoRepository;
import com.pontificia.horarioponti.repository.model.BloqueHorario;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Turno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BloqueHorarioService {

    @Autowired
    private BloqueHorarioRepository bloqueRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    /**
     * Obtiene todos los bloques de horario
     */
    public List<BloqueHorarioDTO> obtenerTodosBloques() {
        return bloqueRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene bloques por turno
     */
    public List<BloqueHorarioDTO> obtenerBloquesPorTurno(Long turnoId) {
        Optional<Turno> turnoOpt = turnoRepository.findById(turnoId);
        if (turnoOpt.isPresent()) {
            return bloqueRepository.findByTurnoOrderByOrden(turnoOpt.get()).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Obtiene bloques disponibles para un ambiente en un día específico
     */
    public List<BloqueHorarioDTO> obtenerBloquesDisponiblesPorAmbienteYDia(Long ambienteId, DisponibilidadDocente.DiaSemana dia) {
        return bloqueRepository.findBloquesDisponiblesByAmbienteAndDia(ambienteId, dia).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un bloque por ID
     */
    public BloqueHorarioDTO obtenerBloquePorId(Long id) {
        Optional<BloqueHorario> bloqueOpt = bloqueRepository.findById(id);
        return bloqueOpt.map(this::convertirADTO).orElse(null);
    }

    /**
     * Crea bloques de horario para un turno
     * Este método crea automáticamente bloques de igual duración para un turno dado
     */
    @Transactional
    public List<BloqueHorarioDTO> crearBloquesPorTurno(Long turnoId, Integer duracionMinutos) {
        Optional<Turno> turnoOpt = turnoRepository.findById(turnoId);
        if (!turnoOpt.isPresent()) {
            return List.of();
        }

        Turno turno = turnoOpt.get();

        // Eliminar bloques existentes para este turno
        List<BloqueHorario> bloquesExistentes = bloqueRepository.findByTurno(turno);
        if (!bloquesExistentes.isEmpty()) {
            // Verificar si alguno tiene asignaciones
            boolean hayAsignaciones = bloquesExistentes.stream()
                    .anyMatch(b -> b.getAsignaciones() != null && !b.getAsignaciones().isEmpty());
            if (hayAsignaciones) {
                return List.of(); // No se pueden modificar bloques con asignaciones
            }

            bloqueRepository.deleteAll(bloquesExistentes);
        }

        // Calcular cuántos bloques caben en el turno
        LocalTime horaInicio = turno.getHoraInicio();
        LocalTime horaFin = turno.getHoraFin();

        // Crear bloques
        List<BloqueHorario> bloques = generarBloques(turno, horaInicio, horaFin, duracionMinutos);

        // Guardar bloques
        List<BloqueHorario> bloquesGuardados = bloqueRepository.saveAll(bloques);

        // Convertir a DTOs
        return bloquesGuardados.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Genera los bloques de horario para un turno
     */
    private List<BloqueHorario> generarBloques(Turno turno, LocalTime horaInicio, LocalTime horaFin, Integer duracionMinutos) {
        List<BloqueHorario> bloques = new ArrayList<>();
        LocalTime horaActual = horaInicio;
        int orden = 1;

        while (horaActual.plusMinutes(duracionMinutos).compareTo(horaFin) <= 0) {
            BloqueHorario bloque = new BloqueHorario();
            bloque.setTurno(turno);
            bloque.setOrden(orden++);
            bloque.setHoraInicio(horaActual);
            LocalTime finBloque = horaActual.plusMinutes(duracionMinutos);
            bloque.setHoraFin(finBloque);
            bloque.setDuracion(duracionMinutos);

            bloques.add(bloque);
            horaActual = finBloque;
        }

        return bloques;
    }

    /**
     * Convierte una entidad BloqueHorario a DTO
     */
    private BloqueHorarioDTO convertirADTO(BloqueHorario bloque) {
        return new BloqueHorarioDTO(
                bloque.getIdBloque(),
                bloque.getTurno().getNombre(),
                bloque.getOrden(),
                bloque.getHoraInicio(),
                bloque.getHoraFin()
        );
    }
}