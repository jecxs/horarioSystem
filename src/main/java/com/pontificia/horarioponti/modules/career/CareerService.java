package com.pontificia.horarioponti.modules.career;

import com.pontificia.horarioponti.modules.career.dto.CareerResponseDto;
import com.pontificia.horarioponti.modules.career.dto.CreateCareerRequestDTO;
import com.pontificia.horarioponti.modules.career.mapper.CareerMapper;
import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.modules.educational_modality.EducationalModalityEntity;
import com.pontificia.horarioponti.modules.educational_modality.EducationalModalityRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CareerService extends BaseService<CareerEntity> {

    @Autowired
    private EducationalModalityRepository modalityRepository;

    @Autowired
    private CareerRepository careerRepository;

    /**
     * Obtiene todas las carreras del sistema en formato DTO.
     *
     * @return Lista de objetos CareerResponseDto.
     */
    public List<CareerResponseDto> getAllCareers() {
        List<CareerEntity> careers = careerRepository.findAll();
        return careers.stream()
                .map(CareerMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva carrera y genera automáticamente los ciclos
     * según los años de duración de la modalidad (2 ciclos por año).
     *
     * @param name       Nombre de la carrera.
     * @param modalityId UUID de la modalidad educativa asociada.
     * @return DTO de la carrera creada.
     */
    @Transactional
    public CareerResponseDto createCareer(String name, UUID modalityId) {
        EducationalModalityEntity modality = modalityRepository.findById(modalityId)
                .orElseThrow(() -> new IllegalArgumentException("La modalidad especificada no existe."));

        if (careerRepository.existsByNameAndModality(name, modality)) {
            throw new IllegalArgumentException("Ya existe una carrera con ese nombre en la modalidad indicada.");
        }

        CareerEntity career = new CareerEntity();
        career.setName(name);
        career.setModality(modality);
        career.setCycles(generateCyclesForCareer(career, modality.getDurationYears()));

        CareerEntity savedCareer = careerRepository.save(career);
        return CareerMapper.toDto(savedCareer);
    }

    /**
     * Actualiza una carrera existente. Si se cambia la modalidad educativa,
     * se eliminan los ciclos anteriores y se generan nuevos.
     *
     * @param careerId ID de la carrera a editar.
     * @param request  Objeto DTO con los nuevos valores.
     * @return DTO de la carrera actualizada.
     */
    @Transactional
    public CareerResponseDto updateCareer(UUID careerId, CreateCareerRequestDTO request) {
        CareerEntity career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));

        if (request.name() != null) {
            career.setName(request.name());
        }

        if (request.modalityId() != null) {
            EducationalModalityEntity newModality = modalityRepository.findById(request.modalityId())
                    .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));

            boolean isDifferentModality = !career.getModality().getUuid().equals(newModality.getUuid());

            if (isDifferentModality) {
                career.setModality(newModality);
                career.getCycles().clear();

                List<CycleEntity> newCycles = generateCyclesForCareer(career, newModality.getDurationYears());
                career.getCycles().addAll(newCycles);
            }
        }

        CareerEntity updatedCareer = careerRepository.save(career);
        return CareerMapper.toDto(updatedCareer);
    }

    /**
     * Obtiene las carreras asociadas a una modalidad educativa.
     *
     * @param modalityId UUID de la modalidad educativa.
     * @return Lista de entidades Career asociadas a la modalidad.
     */
    public List<CareerEntity> obtenerCarrerasPorModalidad(UUID modalityId) {
        Optional<EducationalModalityEntity> modalidadOpt = modalityRepository.findById(modalityId);
        if (modalidadOpt.isPresent()) {
            EducationalModalityEntity modalidad = modalidadOpt.get();
            return careerRepository.findByModality(modalidad);
        }
        return List.of();
    }

    /**
     * Genera la lista de ciclos para una carrera según los años de duración.
     *
     * @param career        Carrera a la que pertenecen los ciclos.
     * @param durationYears Duración en años de la carrera/modalidad.
     * @return Lista de entidades CycleEntity asociadas a la carrera.
     */
    private List<CycleEntity> generateCyclesForCareer(CareerEntity career, int durationYears) {
        List<CycleEntity> cycles = new ArrayList<>();
        for (int i = 1; i <= durationYears * 2; i++) {
            CycleEntity cycle = new CycleEntity();
            cycle.setNumber(i);
            cycle.setCareer(career);
            cycles.add(cycle);
        }
        return cycles;
    }

    /**
     * Elimina una carrera junto con todos sus ciclos relacionados.
     *
     * @param careerId UUID de la carrera a eliminar.
     * @throws EntityNotFoundException si la carrera no existe.
     */
    @Transactional
    public void deleteCareer(UUID careerId) {
        CareerEntity career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));

        careerRepository.delete(career);
    }
}