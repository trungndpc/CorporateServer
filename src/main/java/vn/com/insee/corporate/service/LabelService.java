package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.TypeLabel;
import vn.com.insee.corporate.dto.response.LabelDTO;
import vn.com.insee.corporate.entity.LabelEntity;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.LabelRepository;

import java.util.Optional;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private Mapper mapper;

    public LabelDTO findOrCreate(int id, TypeLabel label) {
        Optional<LabelEntity> optionalLabelEntity = labelRepository.findById(id);
        LabelEntity labelEntity = null;
        if (optionalLabelEntity.isPresent()) {
            labelEntity = optionalLabelEntity.get();
        }
        if (labelEntity == null && label != null) {
            labelEntity = mapper.map(optionalLabelEntity.get(), LabelEntity.class);
            labelEntity = labelRepository.saveAndFlush(labelEntity);
        }
        return mapper.map(labelEntity, LabelDTO.class);
    }

}
