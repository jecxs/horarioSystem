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
     * @return La entidad Career creada, con sus ciclos relacionados.
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

        int totalCycles = modality.getDurationYears() * 2;
        List<CycleEntity> cycles = new ArrayList<>();

        for (int i = 1; i <= totalCycles; i++) {
            CycleEntity cycle = new CycleEntity();
            cycle.setNumber(i);
            cycle.setCareer(career);
            cycles.add(cycle);
        }

        career.setCycles(cycles);
        CareerEntity savedCareer = careerRepository.save(career);

        return CareerMapper.toDto(savedCareer);
    }


    public CareerResponseDto updateCareer(UUID careerId, CreateCareerRequestDTO request) {
        CareerEntity career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));

        if (request.name() != null) {
            career.setName(request.name());
        }

        if (request.modalityId() != null) {
            EducationalModalityEntity modality = modalityRepository.findById(request.modalityId())
                    .orElseThrow(() -> new EntityNotFoundException("Modalidad no encontrada"));
            career.setModality(modality);
        }

        CareerEntity updated = careerRepository.save(career);
        return CareerMapper.toDto(updated);
    }



    /**
     * Obtiene carreras por modalidad
     */
    public List<CareerEntity> obtenerCarrerasPorModalidad(UUID uuid) {
        Optional<EducationalModalityEntity> modalidadOpt = modalityRepository.findById(uuid);
        if (modalidadOpt.isPresent()) {
            EducationalModalityEntity modalidad = modalidadOpt.get();
            return careerRepository.findByModality(modalidad);
        }
        return List.of();
    }

    /**
     * Obtiene todos los ciclos
     */
//    public List<Ciclo> obtenerTodosCiclos() {
//        return cicloRepository.findAll();
//    }

    /**
     * Obtiene ciclos por carrera
     */
//    public List<Ciclo> obtenerCiclosPorCarrera(Long carreraId) {
//        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
//        if (carreraOpt.isPresent()) {
//            Carrera carrera = carreraOpt.get();
//            return cicloRepository.findByCarrera(carrera);
//        }
//        return List.of();
//    }

    /**
     * Crea un nuevo ciclo
     */
//    @Transactional
//    public Ciclo crearCiclo(Integer numero, Long carreraId) {
//        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
//        if (!carreraOpt.isPresent()) {
//            return null;
//        }
//
//        Carrera carrera = carreraOpt.get();
//
//        if (cicloRepository.existsByNumeroAndCarrera(numero, carrera)) {
//            return null;
//        }
//
//        Ciclo ciclo = new Ciclo();
//        ciclo.setNumero(numero);
//        ciclo.setCarrera(carrera);
//        ciclo.setCursos(new ArrayList<>());
//        ciclo.setGrupos(new ArrayList<>());
//
//        return cicloRepository.save(ciclo);
//    }
}
