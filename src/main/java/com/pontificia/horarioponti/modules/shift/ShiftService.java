package com.pontificia.horarioponti.modules.shift;

import com.pontificia.horarioponti.config.ValidationTopicException;
import com.pontificia.horarioponti.modules.shift.dto.ShiftRequestDTO;
import com.pontificia.horarioponti.modules.shift.dto.ShiftResponseDTO;
import com.pontificia.horarioponti.modules.shift.mapper.ShiftMapper;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para gestionar la lógica de negocio relacionada con los turnos.
 * Extiende la clase base para operaciones CRUD genéricas.
 */
@Service
public class ShiftService extends BaseService<ShiftEntity> {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param shiftRepository Repositorio para acceder a los datos de los turnos.
     * @param shiftMapper     Mapper para conversión entre entidad y DTO.
     */
    public ShiftService(ShiftRepository shiftRepository, ShiftMapper shiftMapper) {
        super(shiftRepository);
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
    }

    /**
     * Inicializa los turnos predefinidos si la base de datos está vacía.
     * Esto garantiza que existan bloques horarios básicos al iniciar el sistema.
     */
    @PostConstruct
    @Transactional
    public void initializeShifts() {
        if (shiftRepository.count() == 0) {
            List<ShiftEntity> shifts = new ArrayList<>();

            addShiftGroup(shifts, "M1", new String[][]{
                    {"06:45", "07:30"},
                    {"07:30", "08:15"},
                    {"08:15", "09:00"},
                    {"09:00", "09:45"},
            });

            addShiftGroup(shifts, "M2", new String[][]{
                    {"09:50", "10:35"},
                    {"10:35", "11:20"},
                    {"11:20", "12:05"},
                    {"12:05", "12:50"},
            });

            addShiftGroup(shifts, "T1", new String[][]{
                    {"12:55", "13:40"},
                    {"13:40", "14:25"},
                    {"14:25", "15:10"},
                    {"15:10", "15:55"},
            });

            addShiftGroup(shifts, "T2", new String[][]{
                    {"16:00", "16:45"},
                    {"16:45", "17:30"},
                    {"17:30", "18:15"},
                    {"18:15", "19:00"},
            });

            addShiftGroup(shifts, "N", new String[][]{
                    {"19:05", "19:50"},
                    {"19:50", "20:35"},
                    {"20:35", "21:20"},
                    {"21:20", "22:05"},
            });

            shiftRepository.saveAll(shifts);
        }
    }

    private void addShiftGroup(List<ShiftEntity> list, String code, String[][] times) {
        for (String[] range : times) {
            ShiftEntity shift = new ShiftEntity();
            shift.setCode(code);
            shift.setStartTime(LocalTime.parse(range[0]));
            shift.setEndTime(LocalTime.parse(range[1]));
            list.add(shift);
        }
    }

    /**
     * Obtiene todos los turnos existentes en la base de datos.
     *
     * @return Lista de DTOs {@link ShiftResponseDTO} con la información de los turnos.
     */
    public List<ShiftResponseDTO> getAllShifts() {
        return shiftMapper.toResponseDTOList(findAll());
    }

    /**
     * Crea un nuevo turno.
     * Válida que la hora de inicio sea anterior a la hora de fin.
     *
     * @param requestDTO DTO con los datos del turno a crear.
     * @return DTO con la información del turno creado.
     * @throws ValidationTopicException si la hora de inicio es mayor o igual a la de fin.
     */
    @Transactional
    public ShiftResponseDTO createShift(ShiftRequestDTO requestDTO) {
        if (requestDTO.startTime().isAfter(requestDTO.endTime()) ||
                requestDTO.startTime().equals(requestDTO.endTime())) {
            throw new ValidationTopicException("La hora de inicio debe ser menor que la hora de fin.");
        }

        ShiftEntity shift = shiftMapper.toEntity(requestDTO);
        ShiftEntity savedShift = save(shift);
        return shiftMapper.toResponseDTO(savedShift);
    }

    /**
     * Actualiza un turno existente.
     * Válida que la hora de inicio sea anterior a la hora de fin.
     *
     * @param uuid       UUID del turno a actualizar.
     * @param requestDTO DTO con los nuevos datos del turno.
     * @return DTO con la información del turno actualizado.
     * @throws jakarta.persistence.EntityNotFoundException si el turno no existe.
     * @throws ValidationTopicException                    si la hora de inicio es mayor o igual a la de fin.
     */
    @Transactional
    public ShiftResponseDTO updateShift(UUID uuid, ShiftRequestDTO requestDTO) {
        if (requestDTO.startTime().isAfter(requestDTO.endTime()) ||
                requestDTO.startTime().equals(requestDTO.endTime())) {
            throw new ValidationTopicException("La hora de inicio debe ser menor que la hora de fin.");
        }

        ShiftEntity existingShift = findOrThrow(uuid);
        shiftMapper.updateEntityFromDTO(requestDTO, existingShift);
        ShiftEntity updatedShift = update(existingShift);
        return shiftMapper.toResponseDTO(updatedShift);
    }

    /**
     * Elimina un turno existente identificado por su UUID.
     *
     * @param uuid UUID del turno a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si el turno no existe.
     */
    @Transactional
    public void deleteShift(UUID uuid) {
        findOrThrow(uuid);
        deleteById(uuid);
    }
}
