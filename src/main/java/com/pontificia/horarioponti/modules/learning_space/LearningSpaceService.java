package com.pontificia.horarioponti.modules.learning_space;

import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceRequestDTO;
import com.pontificia.horarioponti.modules.learning_space.dto.LearningSpaceResponseDTO;
import com.pontificia.horarioponti.modules.learning_space.mapper.LearningSpaceMapper;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningSpaceService extends BaseService<LearningSpaceEntity> {
    private final LearningSpaceMapper learningSpaceMapper;

    public LearningSpaceService(LearningSpaceRepository learningSpaceRepository, LearningSpaceMapper learningSpaceMapper) {
        super(learningSpaceRepository);
        this.learningSpaceMapper = learningSpaceMapper;
    }

    public List<LearningSpaceResponseDTO> getAllLearningSpaces() {
        List<LearningSpaceEntity> modalities = findAll();
        return learningSpaceMapper.toResponseDTOList(modalities);
    }

    @Transactional
    public LearningSpaceResponseDTO createLearningSpace(LearningSpaceRequestDTO requestDTO) {
        LearningSpaceEntity modality = learningSpaceMapper.toEntity(requestDTO);
        LearningSpaceEntity savedModality = save(modality);

        return learningSpaceMapper.toResponseDTO(savedModality);
    }

    @Transactional
    public LearningSpaceResponseDTO updateLearningSpace(UUID uuid, LearningSpaceRequestDTO requestDTO) {
        LearningSpaceEntity modality = findOrThrow(uuid);

        learningSpaceMapper.updateEntityFromDTO(requestDTO, modality);
        LearningSpaceEntity updatedModality = update(modality);

        return learningSpaceMapper.toResponseDTO(updatedModality);
    }

    @Transactional
    public void deleteLearningSpace(UUID uuid) {
        findOrThrow(uuid);
        deleteById(uuid);
    }
}
