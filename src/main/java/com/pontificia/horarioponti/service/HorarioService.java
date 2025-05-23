package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.dtos.*;
import com.pontificia.horarioponti.repository.*;
import com.pontificia.horarioponti.repository.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private AsignacionHorarioRepository asignacionRepository;

    @Autowired
    private BloqueHorarioRepository bloqueRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private DisponibilidadDocenteRepository disponibilidadRepository;

    /**
     * Obtiene el horario completo de un docente
     */
    public HorarioDocenteDTO obtenerHorarioDocente(Long docenteId) {
        // Obtener el docente con sus disponibilidades
        Docente docente = docenteRepository.findByIdWithDisponibilidad(docenteId);
        if (docente == null) {
            return null;
        }

        // Convertir docente a DTO
        DocenteDTO docenteDTO = convertirDocenteADTO(docente);

        // Obtener todas las asignaciones del docente
        List<AsignacionHorario> asignaciones = asignacionRepository.findByDocente(docente);

        // Agrupar asignaciones por día
        Map<DisponibilidadDocente.DiaSemana, List<AsignacionHorarioResponseDTO>> asignacionesPorDia =
                new HashMap<>();

        // Inicializar el mapa para todos los días de la semana
        for (DisponibilidadDocente.DiaSemana dia : DisponibilidadDocente.DiaSemana.values()) {
            asignacionesPorDia.put(dia, new ArrayList<>());
        }

        // Agrupar asignaciones por día
        for (AsignacionHorario asignacion : asignaciones) {
            AsignacionHorarioResponseDTO asignacionDTO = convertirAsignacionADTO(asignacion);
            asignacionesPorDia.get(asignacion.getDiaSemana()).add(asignacionDTO);
        }

        // Calcular bloques disponibles por día
        Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> disponibilidadPorDia =
                calcularBloquesDisponibles(docente);

        return new HorarioDocenteDTO(docenteDTO, disponibilidadPorDia, asignacionesPorDia);
    }

    /**
     * Crea una nueva asignación de horario
     */
    @Transactional
    public AsignacionHorarioResponseDTO crearAsignacionHorario(AsignacionHorarioRequestDTO requestDTO) {
        // Verificar existencia de entidades relacionadas
        Optional<Docente> docenteOpt = docenteRepository.findById(requestDTO.getDocenteId());
        Optional<Curso> cursoOpt = cursoRepository.findById(requestDTO.getCursoId());
        Optional<Grupo> grupoOpt = grupoRepository.findById(requestDTO.getGrupoId());
        Optional<Ambiente> ambienteOpt = ambienteRepository.findById(requestDTO.getAmbienteId());

        if (!docenteOpt.isPresent() || !cursoOpt.isPresent() ||
                !grupoOpt.isPresent() || !ambienteOpt.isPresent()) {
            return null;
        }

        // Obtener entidades
        Docente docente = docenteOpt.get();
        Curso curso = cursoOpt.get();
        Grupo grupo = grupoOpt.get();
        Ambiente ambiente = ambienteOpt.get();
        DisponibilidadDocente.DiaSemana diaSemana = requestDTO.getDiaSemana();
        List<Long> bloqueIds = requestDTO.getBloqueIds();

        // Verificar disponibilidad del docente
        if (!verificarDisponibilidadDocente(docente.getIdDocente(), diaSemana, bloqueIds)) {
            return null;
        }

        // Verificar conflictos de horario
        if (asignacionRepository.existsConflictoDocenteEnBloques(docente.getIdDocente(), diaSemana, bloqueIds) ||
                asignacionRepository.existsConflictoAmbienteEnBloques(ambiente.getIdAmbiente(), diaSemana, bloqueIds) ||
                asignacionRepository.existsConflictoGrupoEnBloques(grupo.getIdGrupo(), diaSemana, bloqueIds)) {
            return null;
        }

        // Obtener bloques de horario
        List<BloqueHorario> bloques = bloqueRepository.findAllById(bloqueIds);
        if (bloques.size() != bloqueIds.size()) {
            return null;
        }

        // Crear asignación
        AsignacionHorario asignacion = new AsignacionHorario();
        asignacion.setCurso(curso);
        asignacion.setGrupo(grupo);
        asignacion.setDocente(docente);
        asignacion.setAmbiente(ambiente);
        asignacion.setDiaSemana(diaSemana);
        asignacion.setTipoSesion(requestDTO.getTipoSesion());
        asignacion.setBloques(bloques);

        // Guardar asignación
        AsignacionHorario asignacionGuardada = asignacionRepository.save(asignacion);
        return convertirAsignacionADTO(asignacionGuardada);
    }

    /**
     * Elimina una asignación de horario
     */
    @Transactional
    public boolean eliminarAsignacionHorario(Long id) {
        Optional<AsignacionHorario> asignacionOpt = asignacionRepository.findById(id);
        if (asignacionOpt.isPresent()) {
            asignacionRepository.delete(asignacionOpt.get());
            return true;
        }
        return false;
    }

    /**
     * Verifica la disponibilidad del docente para los bloques solicitados
     */
    private boolean verificarDisponibilidadDocente(Long docenteId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds) {
        // Obtener bloques solicitados
        List<BloqueHorario> bloquesSolicitados = bloqueRepository.findAllById(bloqueIds);
        if (bloquesSolicitados.isEmpty()) {
            return false;
        }

        // Encontrar hora inicio y fin de los bloques
        LocalTime horaInicio = bloquesSolicitados.stream()
                .min(Comparator.comparing(BloqueHorario::getHoraInicio))
                .map(BloqueHorario::getHoraInicio)
                .orElse(null);

        LocalTime horaFin = bloquesSolicitados.stream()
                .max(Comparator.comparing(BloqueHorario::getHoraFin))
                .map(BloqueHorario::getHoraFin)
                .orElse(null);

        if (horaInicio == null || horaFin == null) {
            return false;
        }

        // Obtener docente
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (!docenteOpt.isPresent()) {
            return false;
        }
        Docente docente = docenteOpt.get();

        // En lugar de usar el método del repositorio que está causando el error,
        // obtenemos primero todas las disponibilidades para el día y luego filtramos manualmente
        List<DisponibilidadDocente> disponibilidadesDia = disponibilidadRepository
                .findByDocenteAndDiaSemana(docente, diaSemana);

        // Verificar que al menos una disponibilidad cubra todo el rango requerido
        boolean disponible = disponibilidadesDia.stream()
                .anyMatch(disp ->
                        (disp.getHoraInicio().compareTo(horaInicio) <= 0 &&
                                disp.getHoraFin().compareTo(horaFin) >= 0)
                );

        return disponible;
    }

    /**
     * Calcula los bloques disponibles para cada día de la semana para un docente
     */
    private Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> calcularBloquesDisponibles(Docente docente) {
        Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> disponibilidadPorDia = new HashMap<>();

        // Inicializar el mapa para todos los días de la semana
        for (DisponibilidadDocente.DiaSemana dia : DisponibilidadDocente.DiaSemana.values()) {
            disponibilidadPorDia.put(dia, new ArrayList<>());
        }

        // Obtener todos los bloques de horario
        List<BloqueHorario> todosLosBloques = bloqueRepository.findAll();

        // Para cada día, calcular disponibilidad
        for (DisponibilidadDocente.DiaSemana dia : DisponibilidadDocente.DiaSemana.values()) {
            // Obtener disponibilidades del docente para este día
            List<DisponibilidadDocente> disponibilidadesDia = disponibilidadRepository
                    .findByDocenteAndDiaSemana(docente, dia);

            // Obtener asignaciones del docente para este día
            List<AsignacionHorario> asignacionesDia = asignacionRepository
                    .findByDocenteAndDia(docente.getIdDocente(), dia);

            // Conjunto de IDs de bloques ya asignados
            Set<Long> bloquesAsignados = asignacionesDia.stream()
                    .flatMap(a -> a.getBloques().stream().map(BloqueHorario::getIdBloque))
                    .collect(Collectors.toSet());

            // Para cada bloque, verificar disponibilidad
            for (BloqueHorario bloque : todosLosBloques) {
                boolean bloqueDisponible = !bloquesAsignados.contains(bloque.getIdBloque()) &&
                        disponibilidadesDia.stream().anyMatch(d ->
                                bloque.getHoraInicio().compareTo(d.getHoraInicio()) >= 0 &&
                                        bloque.getHoraFin().compareTo(d.getHoraFin()) <= 0);

                BloqueDisponibleDTO bloqueDTO = new BloqueDisponibleDTO(
                        bloque.getIdBloque(),
                        bloque.getHoraInicio(),
                        bloque.getHoraFin(),
                        bloqueDisponible
                );
                disponibilidadPorDia.get(dia).add(bloqueDTO);
            }
        }

        return disponibilidadPorDia;
    }

    // Métodos auxiliares para conversión de entidades a DTOs

    private DocenteDTO convertirDocenteADTO(Docente docente) {
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

    private DisponibilidadDocenteDTO convertirDisponibilidadADTO(DisponibilidadDocente disponibilidad) {
        DisponibilidadDocenteDTO dto = new DisponibilidadDocenteDTO();
        dto.setId(disponibilidad.getId());
        dto.setDiaSemana(disponibilidad.getDiaSemana());
        dto.setHoraInicio(disponibilidad.getHoraInicio());
        dto.setHoraFin(disponibilidad.getHoraFin());
        return dto;
    }

    private AsignacionHorarioResponseDTO convertirAsignacionADTO(AsignacionHorario asignacion) {
        // Convertir curso
        CursoDTO cursoDTO = new CursoDTO(
                asignacion.getCurso().getIdCurso(),
                asignacion.getCurso().getNombre(),
                asignacion.getCurso().getTipo(),
                asignacion.getCurso().getHorasSemana(),
                asignacion.getCurso().getCiclo().getNumero().toString(),
                asignacion.getCurso().getCiclo().getIdCiclo(), // Añadir el ID del ciclo
                asignacion.getCurso().getCiclo().getCarrera().getNombre(),
                asignacion.getCurso().getCiclo().getCarrera().getModalidad().getNombre()
        );

        // Convertir grupo
        GrupoDTO grupoDTO = new GrupoDTO(
                asignacion.getGrupo().getIdGrupo(),
                asignacion.getGrupo().getNombre(),
                asignacion.getGrupo().getCiclo().getIdCiclo(),
                "Ciclo " + asignacion.getGrupo().getCiclo().getNumero()
        );

        // Convertir docente (versión simplificada sin disponibilidades para evitar ciclos)
        DocenteDTO docenteDTO = new DocenteDTO(
                asignacion.getDocente().getIdDocente(),
                asignacion.getDocente().getNombreCompleto(),
                asignacion.getDocente().getEspecialidad(),
                new ArrayList<>()
        );

        // Convertir ambiente
        AmbienteDTO ambienteDTO = new AmbienteDTO(
                asignacion.getAmbiente().getIdAmbiente(),
                asignacion.getAmbiente().getNombre(),
                asignacion.getAmbiente().getTipo(),
                asignacion.getAmbiente().getCapacidad()
        );

        // Convertir bloques
        List<BloqueHorarioDTO> bloquesDTO = asignacion.getBloques().stream()
                .map(b -> new BloqueHorarioDTO(
                        b.getIdBloque(),
                        b.getTurno().getNombre(),
                        b.getOrden(),
                        b.getHoraInicio(),
                        b.getHoraFin()
                ))
                .collect(Collectors.toList());

        return new AsignacionHorarioResponseDTO(
                asignacion.getId(),
                cursoDTO,
                grupoDTO,
                docenteDTO,
                ambienteDTO,
                asignacion.getDiaSemana(),
                asignacion.getTipoSesion(),
                bloquesDTO
        );
    }
}
