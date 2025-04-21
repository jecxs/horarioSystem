package com.pontificia.horarioponti.modules.ModalidadEducativa;

import com.pontificia.horarioponti.mapper.EducationalModalityMapper;
import com.pontificia.horarioponti.payload.request.EducationalModalityRequestDTO;
import com.pontificia.horarioponti.payload.response.EducationalModalityResponseDTO;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EducationalModalityService extends BaseService<EducationalModality> {

    private final EducationalModalityRepository modalityRepository;
    private final EducationalModalityMapper modalityMapper;

    @Autowired
    public EducationalModalityService(EducationalModalityRepository modalityRepository,
                                      EducationalModalityMapper modalityMapper) {
        this.modalityRepository = modalityRepository;
        this.modalityMapper = modalityMapper;
    }

    @Override
    protected BaseRepository<EducationalModality> getRepository() {
        return modalityRepository;
    }


    public List<EducationalModalityResponseDTO> getAllModalities() {
        List<EducationalModality> modalities = findAll();
        return modalityMapper.toResponseDTOList(modalities);
    }


    public EducationalModalityResponseDTO getModalityById(UUID uuid) {
        EducationalModality modality = findModalityOrThrow(uuid);
        return modalityMapper.toResponseDTO(modality);
    }


    @Transactional
    public EducationalModalityResponseDTO createModality(EducationalModalityRequestDTO requestDTO) {
        // Verificar que no exista una modalidad con el mismo nombre
        if (modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe una modalidad educativa con el nombre: " + requestDTO.getName());
        }

        EducationalModality modality = modalityMapper.toEntity(requestDTO);
        EducationalModality savedModality = save(modality);

        return modalityMapper.toResponseDTO(savedModality);
    }


    @Transactional
    public EducationalModalityResponseDTO updateModality(UUID uuid, EducationalModalityRequestDTO requestDTO) {
        EducationalModality modality = findModalityOrThrow(uuid);

        // Verificar que no exista otra modalidad con el mismo nombre (excepto esta misma)
        if (!modality.getName().equals(requestDTO.getName()) &&
                modalityRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe otra modalidad educativa con el nombre: " + requestDTO.getName());
        }

        modalityMapper.updateEntityFromDTO(requestDTO, modality);
        EducationalModality updatedModality = update(modality);

        return modalityMapper.toResponseDTO(updatedModality);
    }


    @Transactional
    public void deleteModality(UUID uuid) {
        EducationalModality modality = findModalityOrThrow(uuid);

        deleteById(uuid);
    }


    private EducationalModality findModalityOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Modalidad educativa no encontrada con ID: " + uuid));
    }
}
