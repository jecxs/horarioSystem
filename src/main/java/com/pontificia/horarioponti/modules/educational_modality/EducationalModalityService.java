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


    public List<EducationalModalityResponseDTO> getAllModalities() {
        List<EducationalModalityEntity> modalities = findAll();
        return modalityMapper.toResponseDTOList(modalities);
    }

    public EducationalModalityResponseDTO getModalityById(UUID uuid) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);
        return modalityMapper.toResponseDTO(modality);
    }

    @Transactional
    public EducationalModalityResponseDTO createModality(EducationalModalityRequestDTO requestDTO) {
        // Verificar que no exista una modalidad con el mismo nombre
        if (modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe modalidad educativa " + requestDTO.getName());
        }

        EducationalModalityEntity modality = modalityMapper.toEntity(requestDTO);
        EducationalModalityEntity savedModality = save(modality);

        return modalityMapper.toResponseDTO(savedModality);
    }

    @Transactional
    public EducationalModalityResponseDTO updateModality(UUID uuid, EducationalModalityRequestDTO requestDTO) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);

        // Verificar que no exista otra modalidad con el mismo nombre (excepto esta misma)
        if (!modality.getName().equals(requestDTO.getName()) &&
                modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe otra modalidad educativa con el nombre: " + requestDTO.getName());
        }

        modalityMapper.updateEntityFromDTO(requestDTO, modality);
        EducationalModalityEntity updatedModality = update(modality);

        return modalityMapper.toResponseDTO(updatedModality);
    }

    @Transactional
    public void deleteModality(UUID uuid) {
        EducationalModalityEntity modality = findModalityOrThrow(uuid);

        deleteById(uuid);
    }

    private EducationalModalityEntity findModalityOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Modalidad educativa no encontrada con ID: " + uuid));
    }
}
