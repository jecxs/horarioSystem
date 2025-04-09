package com.pontificia.horarioponti.service;

import com.pontificia.horarioponti.repository.ModalidadEducativaRepository;
import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModalidadEducativaService {

    private final ModalidadEducativaRepository modalidadRepository;

    public List<ModalidadEducativa> getAllModalidades() {
        return modalidadRepository.findAll();
    }

    public ModalidadEducativa getModalidadById(Long id) {
        return modalidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidad educativa no encontrada con ID: " + id));
    }

    public ModalidadEducativa getModalidadByNombre(String nombre) {
        return modalidadRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Modalidad educativa no encontrada con nombre: " + nombre));
    }

    @Transactional
    public ModalidadEducativa createModalidad(ModalidadEducativa modalidad) {
        // Validar que no exista una modalidad con el mismo nombre
        if (modalidadRepository.existsByNombre(modalidad.getNombre())) {
            throw new RuntimeException("Ya existe una modalidad educativa con el nombre: " + modalidad.getNombre());
        }

        return modalidadRepository.save(modalidad);
    }

    @Transactional
    public ModalidadEducativa updateModalidad(Long id, ModalidadEducativa modalidadDetails) {
        ModalidadEducativa modalidad = modalidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidad educativa no encontrada con ID: " + id));

        // Validar que no exista otra modalidad con el mismo nombre
        if (!modalidad.getNombre().equals(modalidadDetails.getNombre()) &&
                modalidadRepository.existsByNombre(modalidadDetails.getNombre())) {
            throw new RuntimeException("Ya existe una modalidad educativa con el nombre: " + modalidadDetails.getNombre());
        }

        modalidad.setNombre(modalidadDetails.getNombre());
        modalidad.setDuracionAnios(modalidadDetails.getDuracionAnios());

        return modalidadRepository.save(modalidad);
    }

    @Transactional
    public void deleteModalidad(Long id) {
        ModalidadEducativa modalidad = modalidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidad educativa no encontrada con ID: " + id));

        // Verificar si tiene carreras asociadas
        if (modalidad.getCarreras() != null && !modalidad.getCarreras().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la modalidad porque tiene carreras asociadas");
        }

        modalidadRepository.delete(modalidad);
    }

    // Método para inicializar las modalidades educativas básicas
    @Transactional
    public void inicializarModalidadesBasicas() {
        // Verificar si ya existen modalidades
        if (!modalidadRepository.findAll().isEmpty()) {
            return; // Si ya hay modalidades, no crear más
        }

        // Crear modalidad "Instituto"
        ModalidadEducativa instituto = new ModalidadEducativa();
        instituto.setNombre("Instituto La Pontificia");
        instituto.setDuracionAnios(3);
        modalidadRepository.save(instituto);

        // Crear modalidad "Escuela Superior"
        ModalidadEducativa escuelaSuperior = new ModalidadEducativa();
        escuelaSuperior.setNombre("Escuela Superior La Pontificia");
        escuelaSuperior.setDuracionAnios(5);
        modalidadRepository.save(escuelaSuperior);
    }
}
