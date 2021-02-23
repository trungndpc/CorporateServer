package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.type.TypeLabel;
import vn.com.insee.corporate.dto.response.LabelDTO;
import vn.com.insee.corporate.entity.LabelEntity;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.LabelRepository;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private Mapper mapper;

    public LabelDTO create(String name, TypeLabel typeLabel) {
        LabelEntity entity = new LabelEntity();
        entity.setName(name);
        entity.setType(typeLabel.getType());
        labelRepository.saveAndFlush(entity);
        return mapper.map(entity, LabelDTO.class);
    }

    public List<LabelDTO> findByType(TypeLabel typeLabel) {
        List<LabelEntity> labelEntities = labelRepository.findByType(typeLabel.getType());
        List<LabelDTO> labelDTOS = mapper.mapToList(labelEntities, new TypeToken<List<LabelDTO>>() {
        }.getType());
        return labelDTOS;
    }

    public LabelDTO get(int id) {
        return mapper.map(labelRepository.getOne(id), LabelDTO.class);
    }

}
