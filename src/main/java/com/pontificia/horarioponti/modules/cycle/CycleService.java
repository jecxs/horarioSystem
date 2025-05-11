package com.pontificia.horarioponti.modules.cycle;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CycleService {

    private final CycleRepository cycleRepository;

    /**
     * Crea un nuevo ciclo académico.
     *
     * @param request Entidad CycleEntity con la información del ciclo a registrar.
     * @return El ciclo creado y persistido en la base de datos.
     * @throws IllegalArgumentException si los datos requeridos no son válidos.
     */
    @Transactional
    public CycleEntity createCycle(CycleEntity request) {
        if (request.getNumber() == null || request.getCareer() == null) {
            throw new IllegalArgumentException("El número del ciclo y el curso son obligatorios.");
        }

        return cycleRepository.save(request);
    }
}
