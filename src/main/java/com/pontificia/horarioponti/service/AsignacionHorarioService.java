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
public class AsignacionHorarioService {

    @Autowired
    private AsignacionHorarioRepository asignacionRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private BloqueHorarioRepository bloqueRepository;

    @Autowired
    private DisponibilidadDocenteRepository disponibilidadRepository;

    /**
     * Obtiene todas las asignaciones de horario
     */
    public List<AsignacionHorarioResponseDTO> obtenerTodasAsignaciones() {
        return asignacionRepository.findAll().stream()
                .map(this::convertirAsignacionADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene asignaciones por docente
     */
    public List<AsignacionHorarioResponseDTO> obtenerAsignacionesPorDocente(Long docenteId) {
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            return asignacionRepository.findByDocente(docente).stream()
                    .map(this::convertirAsignacionADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Obtiene asignaciones por grupo
     */
    public List<AsignacionHorarioResponseDTO> obtenerAsignacionesPorGrupo(Long grupoId) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            return asignacionRepository.findByGrupo(grupo).stream()
                    .map(this::convertirAsignacionADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Obtiene asignaciones por ambiente
     */
    public List<AsignacionHorarioResponseDTO> obtenerAsignacionesPorAmbiente(Long ambienteId) {
        Optional<Ambiente> ambienteOpt = ambienteRepository.findById(ambienteId);
        if (ambienteOpt.isPresent()) {
            Ambiente ambiente = ambienteOpt.get();
            return asignacionRepository.findByAmbiente(ambiente).stream()
                    .map(this::convertirAsignacionADTO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Verifica si un docente está disponible para los bloques solicitados
     */
    public boolean verificarDisponibilidadDocente(
            Long docenteId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds) {

        // Obtener bloques solicitados
        List<BloqueHorario> bloquesSolicitados = bloqueRepository.findAllById(bloqueIds);
        if (bloquesSolicitados.isEmpty() || bloquesSolicitados.size() != bloqueIds.size()) {
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

        // Verificar conflictos existentes
        if (asignacionRepository.existsConflictoDocenteEnBloques(docenteId, diaSemana, bloqueIds)) {
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
     * Verifica si un ambiente está disponible para los bloques solicitados
     */
    public boolean verificarDisponibilidadAmbiente(
            Long ambienteId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds) {

        return !asignacionRepository.existsConflictoAmbienteEnBloques(ambienteId, diaSemana, bloqueIds);
    }

    /**
     * Verifica si un grupo está disponible para los bloques solicitados
     */
    public boolean verificarDisponibilidadGrupo(
            Long grupoId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds) {

        return !asignacionRepository.existsConflictoGrupoEnBloques(grupoId, diaSemana, bloqueIds);
    }

    /**
     * Modifica el método crearAsignacionHorario para validar las horas
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
        AsignacionHorario.TipoSesion tipoSesion = requestDTO.getTipoSesion();
        List<Long> bloqueIds = requestDTO.getBloqueIds();

        // Verificar que el tipo de sesión sea compatible con el tipo de curso
        if (curso.getTipo() == Curso.TipoCurso.Teorico && tipoSesion != AsignacionHorario.TipoSesion.Teorica) {
            throw new IllegalArgumentException("El curso es teórico y solo admite sesiones teóricas");
        }

        if (curso.getTipo() == Curso.TipoCurso.Practico && tipoSesion != AsignacionHorario.TipoSesion.Practica) {
            throw new IllegalArgumentException("El curso es práctico y solo admite sesiones prácticas");
        }


        // Verificar disponibilidad del docente
        if (!verificarDisponibilidadDocente(docente.getIdDocente(), diaSemana, bloqueIds)) {
            return null;
        }

        // Verificar disponibilidad del ambiente
        if (!verificarDisponibilidadAmbiente(ambiente.getIdAmbiente(), diaSemana, bloqueIds)) {
            return null;
        }

        // Verificar disponibilidad del grupo
        if (!verificarDisponibilidadGrupo(grupo.getIdGrupo(), diaSemana, bloqueIds)) {
            return null;
        }

        // Obtener bloques de horario
        List<BloqueHorario> bloques = bloqueRepository.findAllById(bloqueIds);
        if (bloques.size() != bloqueIds.size()) {
            return null;
        }

        // Verificar coherencia con el tipo de ambiente y sesión
        boolean esCompatible = (tipoSesion == AsignacionHorario.TipoSesion.Teorica && ambiente.getTipo() == Ambiente.TipoAmbiente.Teorico) ||
                (tipoSesion == AsignacionHorario.TipoSesion.Practica && ambiente.getTipo() == Ambiente.TipoAmbiente.Practico);

        if (!esCompatible) {
            throw new IllegalArgumentException("El tipo de ambiente no es compatible con el tipo de sesión");
        }

        // VALIDACIÓN DE HORAS: Verificar que no se exceda el total de horas semanales
        int horasYaAsignadas = calcularHorasAsignadas(curso.getIdCurso(), grupo.getIdGrupo());

        // Calcular las horas de la nueva asignación
        int totalMinutosNuevos = 0;
        for (BloqueHorario bloque : bloques) {
            totalMinutosNuevos += bloque.getDuracion();
        }
        int horasNuevaAsignacion = totalMinutosNuevos / 45;

        // Verificar que no se exceda el límite
        if (horasYaAsignadas + horasNuevaAsignacion > curso.getHorasSemana()) {
            throw new IllegalStateException(
                    "La asignación excede el total de horas semanales permitidas para este curso. " +
                            "Horas ya asignadas: " + horasYaAsignadas + ", " +
                            "Horas a asignar: " + horasNuevaAsignacion + ", " +
                            "Horas semanales del curso: " + curso.getHorasSemana()
            );
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
     * Obtiene un horario completo para un docente
     */
    public HorarioDocenteDTO obtenerHorarioDocente(Long docenteId) {
        // Obtener el docente con sus disponibilidades
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        if (!docenteOpt.isPresent()) {
            return null;
        }

        Docente docente = docenteOpt.get();

        // Convertir docente a DTO (sin disponibilidades para evitar ciclos)
        DocenteDTO docenteDTO = new DocenteDTO(
                docente.getIdDocente(),
                docente.getNombreCompleto(),
                docente.getEspecialidad(),
                docente.getDisponibilidades().stream()
                        .map(this::convertirDisponibilidadADTO)
                        .collect(Collectors.toList())
        );

        // Obtener todas las asignaciones del docente
        List<AsignacionHorario> asignaciones = asignacionRepository.findByDocente(docente);

        // Agrupar asignaciones por día
        Map<DisponibilidadDocente.DiaSemana, List<AsignacionHorarioResponseDTO>> asignacionesPorDia =
                new EnumMap<>(DisponibilidadDocente.DiaSemana.class);

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
     * Calcula los bloques disponibles para cada día de la semana para un docente
     */
    private Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> calcularBloquesDisponibles(Docente docente) {
        Map<DisponibilidadDocente.DiaSemana, List<BloqueDisponibleDTO>> disponibilidadPorDia =
                new EnumMap<>(DisponibilidadDocente.DiaSemana.class);

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
                boolean bloqueEnDisponibilidad = disponibilidadesDia.stream()
                        .anyMatch(d ->
                                bloque.getHoraInicio().compareTo(d.getHoraInicio()) >= 0 &&
                                        bloque.getHoraFin().compareTo(d.getHoraFin()) <= 0);

                boolean bloqueDisponible = bloqueEnDisponibilidad && !bloquesAsignados.contains(bloque.getIdBloque());

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

    /**
     * Convierte una entidad DisponibilidadDocente a DTO
     */
    private DisponibilidadDocenteDTO convertirDisponibilidadADTO(DisponibilidadDocente disponibilidad) {
        return new DisponibilidadDocenteDTO(
                disponibilidad.getId(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin()
        );
    }
    /**
     * Calcula las horas pedagógicas ya asignadas para un curso y grupo específico
     */
    public int calcularHorasAsignadas(Long cursoId, Long grupoId) {
        // Obtener las entidades
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);

        if (!cursoOpt.isPresent() || !grupoOpt.isPresent()) {
            return 0;
        }

        Curso curso = cursoOpt.get();
        Grupo grupo = grupoOpt.get();

        // Buscar todas las asignaciones para este curso y grupo
        List<AsignacionHorario> asignaciones = asignacionRepository.findByCursoAndGrupo(curso, grupo);

        // Calcular la duración total en minutos
        int totalMinutos = 0;
        for (AsignacionHorario asignacion : asignaciones) {
            for (BloqueHorario bloque : asignacion.getBloques()) {
                totalMinutos += bloque.getDuracion();
            }
        }

        // Convertir minutos a horas pedagógicas (45 minutos cada una)
        return totalMinutos / 45;
    }

    /**
     * Convierte una entidad AsignacionHorario a DTO
     */
    private AsignacionHorarioResponseDTO convertirAsignacionADTO(AsignacionHorario asignacion) {
        // Convertir curso
        CursoDTO cursoDTO = new CursoDTO(
                asignacion.getCurso().getIdCurso(),
                asignacion.getCurso().getNombre(),
                asignacion.getCurso().getTipo(),
                asignacion.getCurso().getHorasSemana(),
                asignacion.getCurso().getCiclo().getNumero().toString(),
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
