package com.pontificia.horarioponti.modules.educational_modality;

import com.pontificia.horarioponti.modules.educational_modality.mapper.EducationalModalityMapper;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityRequestDTO;
import com.pontificia.horarioponti.modules.educational_modality.dto.EducationalModalityResponseDTO;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EducationalModalityService extends BaseService<EducationalModalityEntity> {

    private final EducationalModalityRepository modalityRepository;
    private final EducationalModalityMapper modalityMapper;

    public EducationalModalityService(EducationalModalityRepository modalityRepository,
                                      EducationalModalityMapper modalityMapper) {
        super(modalityRepository);
        this.modalityRepository = modalityRepository;
        this.modalityMapper = modalityMapper;
    }


    /**
     * Recupera todas las modalidades educativas disponibles.
     *
     * @return Lista de DTOs de modalidades educativas.
     */
    public List<EducationalModalityResponseDTO> getAllModalities() {
        List<EducationalModalityEntity> modalities = findAll();
        return modalityMapper.toResponseDTOList(modalities);
    }

    /**
     * Recupera una modalidad educativa por su ID.
     *
     * @param uuid UUID de la modalidad educativa a recuperar.
     * @return DTO de modalidad educativa.
     */
    public EducationalModalityResponseDTO getModalityById(UUID uuid) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);
        return modalityMapper.toResponseDTO(modality);
    }

    /**
     * Crea una nueva modalidad educativa.
     *
     * @param requestDTO DTO con los datos de la modalidad educativa a crear.
     * @return DTO de la modalidad educativa creada.
     * @throws IllegalArgumentException si ya existe una modalidad educativa con el mismo nombre.
     */
    @Transactional
    public EducationalModalityResponseDTO createModality(EducationalModalityRequestDTO requestDTO) {
        if (modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe modalidad educativa " + requestDTO.getName());
        }

        EducationalModalityEntity modality = modalityMapper.toEntity(requestDTO);
        EducationalModalityEntity savedModality = save(modality);

        return modalityMapper.toResponseDTO(savedModality);
    }

    /**
     * Actualiza los datos de una modalidad educativa existente.
     *
     * @param uuid UUID de la modalidad educativa a actualizar.
     * @param requestDTO DTO con los datos actualizados de la modalidad educativa.
     * @return DTO de la modalidad educativa actualizada.
     * @throws IllegalArgumentException si ya existe otra modalidad con el mismo nombre.
     */
    @Transactional
    public EducationalModalityResponseDTO updateModality(UUID uuid, EducationalModalityRequestDTO requestDTO) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);

        if (!modality.getName().equals(requestDTO.getName()) &&
                modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe otra modalidad educativa con el nombre: " + requestDTO.getName());
        }

        modalityMapper.updateEntityFromDTO(requestDTO, modality);
        EducationalModalityEntity updatedModality = update(modality);

        return modalityMapper.toResponseDTO(updatedModality);
    }

    /**
     * Elimina una modalidad educativa por su ID.
     *
     * @param uuid UUID de la modalidad educativa a eliminar.
     */
    @Transactional
    public void deleteModality(UUID uuid) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);

        deleteById(uuid);
    }

    /**
     * Recupera una modalidad educativa por su UUID, lanzando una excepción si no se encuentra.
     *
     * @param uuid UUID de la modalidad educativa.
     * @return La entidad EducationalModalityEntity correspondiente al UUID.
     * @throws EntityNotFoundException si no se encuentra la modalidad educativa.
     */
    private EducationalModalityEntity findModalityOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Modalidad educativa no encontrada con ID: " + uuid));
    }
}
