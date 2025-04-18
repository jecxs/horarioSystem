package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.repository.CarreraRepository;
import com.pontificia.horarioponti.repository.CicloRepository;
import com.pontificia.horarioponti.repository.ModalidadEducativaRepository;
import com.pontificia.horarioponti.repository.model.Carrera;
import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {

    @Autowired
    private ModalidadEducativaRepository modalidadRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private CicloRepository cicloRepository;

    /**
     * Obtiene todas las modalidades educativas
     */
    public List<ModalidadEducativa> obtenerTodasModalidades() {
        return modalidadRepository.findAll();
    }

    /**
     * Crea una nueva modalidad educativa
     */
    @Transactional
    public ModalidadEducativa crearModalidad(String nombre, Integer duracionAnios) {
        if (modalidadRepository.existsByNombre(nombre)) {
            return null;
        }

        ModalidadEducativa modalidad = new ModalidadEducativa();
        modalidad.setNombre(nombre);
        modalidad.setDuracionAnios(duracionAnios);
        modalidad.setCarreras(new ArrayList<>());

        return modalidadRepository.save(modalidad);
    }

    /**
     * Obtiene todas las carreras
     */
    public List<Carrera> obtenerTodasCarreras() {
        return carreraRepository.findAll();
    }

    /**
     * Obtiene carreras por modalidad
     */
    public List<Carrera> obtenerCarrerasPorModalidad(Long modalidadId) {
        Optional<ModalidadEducativa> modalidadOpt = modalidadRepository.findById(modalidadId);
        if (modalidadOpt.isPresent()) {
            ModalidadEducativa modalidad = modalidadOpt.get();
            return carreraRepository.findByModalidad(modalidad);
        }
        return List.of();
    }

    /**
     * Crea una nueva carrera
     */
    @Transactional
    public Carrera crearCarrera(String nombre, Long modalidadId) {
        Optional<ModalidadEducativa> modalidadOpt = modalidadRepository.findById(modalidadId);
        if (!modalidadOpt.isPresent()) {
            return null;
        }

        ModalidadEducativa modalidad = modalidadOpt.get();

        if (carreraRepository.existsByNombreAndModalidad(nombre, modalidad)) {
            return null;
        }

        Carrera carrera = new Carrera();
        carrera.setNombre(nombre);
        carrera.setModalidad(modalidad);
        carrera.setCiclos(new ArrayList<>());

        return carreraRepository.save(carrera);
    }

    /**
     * Obtiene todos los ciclos
     */
    public List<Ciclo> obtenerTodosCiclos() {
        return cicloRepository.findAll();
    }

    /**
     * Obtiene ciclos por carrera
     */
    public List<Ciclo> obtenerCiclosPorCarrera(Long carreraId) {
        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
        if (carreraOpt.isPresent()) {
            Carrera carrera = carreraOpt.get();
            return cicloRepository.findByCarrera(carrera);
        }
        return List.of();
    }

    /**
     * Crea un nuevo ciclo
     */
    @Transactional
    public Ciclo crearCiclo(Integer numero, Long carreraId) {
        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
        if (!carreraOpt.isPresent()) {
            return null;
        }

        Carrera carrera = carreraOpt.get();

        if (cicloRepository.existsByNumeroAndCarrera(numero, carrera)) {
            return null;
        }

        Ciclo ciclo = new Ciclo();
        ciclo.setNumero(numero);
        ciclo.setCarrera(carrera);
        ciclo.setCursos(new ArrayList<>());
        ciclo.setGrupos(new ArrayList<>());

        return cicloRepository.save(ciclo);
    }
    /**
     * Actualiza una modalidad educativa existente
     */
    @Transactional
    public ModalidadEducativa actualizarModalidad(Long id, String nombre, Integer duracionAnios) {
        Optional<ModalidadEducativa> modalidadOpt = modalidadRepository.findById(id);
        if (!modalidadOpt.isPresent()) {
            return null;
        }

        ModalidadEducativa modalidad = modalidadOpt.get();

        // Verificar que no exista otra modalidad con el mismo nombre
        if (!modalidad.getNombre().equals(nombre)) {
            boolean existeOtra = modalidadRepository.existsByNombre(nombre);
            if (existeOtra) {
                return null;
            }
        }

        // Actualizar datos
        modalidad.setNombre(nombre);
        modalidad.setDuracionAnios(duracionAnios);

        return modalidadRepository.save(modalidad);
    }

    /**
     * Elimina una modalidad educativa si no tiene carreras asociadas
     */
    @Transactional
    public boolean eliminarModalidad(Long id) {
        Optional<ModalidadEducativa> modalidadOpt = modalidadRepository.findById(id);
        if (modalidadOpt.isPresent()) {
            ModalidadEducativa modalidad = modalidadOpt.get();

            // Verificar si tiene carreras asociadas
            if (modalidad.getCarreras() == null || modalidad.getCarreras().isEmpty()) {
                modalidadRepository.delete(modalidad);
                return true;
            }
        }
        return false;
    }
}
