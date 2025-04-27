package com.pontificia.horarioponti.modules.Career;

import com.pontificia.horarioponti.modules.ModalidadEducativa.EducationalModality;
import com.pontificia.horarioponti.modules.ModalidadEducativa.EducationalModalityRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CareerService extends BaseService<Career> {
    @Override
    protected BaseRepository<Career> getRepository() {
        return null;
    }

    @Autowired
    private EducationalModalityRepository modalityRepository;

    @Autowired
    private CareerRepository careerRepository;

//    @Autowired
//    private CycleRepository cycleRepository;

    /**
     * Obtiene todas las modalidades educativas
     */
    public List<EducationalModality> obtenerTodasModalidades() {
        return modalityRepository.findAll();
    }

    /**
     * Crea una nueva modalidad educativa
     */
    @Transactional
    public EducationalModality crearModalidad(String nombre, Integer durationYears) {
        if (modalityRepository.existsByName(nombre)) {
            return null;
        }

        EducationalModality modality = new EducationalModality();
        modality.setName(nombre);
        modality.setDurationYears(durationYears);
        /* modality.setCareers(new ArrayList<>()); */

        return modalityRepository.save(modality);
    }

    /**
     * Obtiene todas las carreras
     */
    public List<Career> obtenerTodasCarreras() {
        return careerRepository.findAll();
    }

    /**
     * Obtiene carreras por modalidad
     */
    public List<Career> obtenerCarrerasPorModalidad(UUID uuid) {
        Optional<EducationalModality> modalidadOpt = modalityRepository.findById(uuid);
        if (modalidadOpt.isPresent()) {
            EducationalModality modalidad = modalidadOpt.get();
            return careerRepository.findByModality(modalidad);
        }
        return List.of();
    }

    /**
     * Crea una nueva carrera
     */
    @Transactional
    public Career createCareer(String name, UUID uuid) {
        Optional<EducationalModality> modalidadOpt = modalityRepository.findById(uuid);
        if (!modalidadOpt.isPresent()) {
            return null;
        }

        EducationalModality modality = modalidadOpt.get();

        if (careerRepository.existsByNameAndModality(name, modality)) {
            return null;
        }

        Career career = new Career();
        career.setName(name);
        career.setModality(modality);
//        career.setCiclos(new ArrayList<>());

        return careerRepository.save(career);
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
