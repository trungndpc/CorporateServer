package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.PromotionStatus;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.entity.PromotionEntity;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.PromotionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private Mapper mapper;

    public PageDTO<PromotionDTO> getList(int page, int pageSize) {
        Page<PromotionEntity> postEntities = promotionRepository.findAll(PageRequest.of(page, pageSize));
        List<PromotionDTO> promotionDTOList = mapper.mapToList(postEntities.toList(), new TypeToken<List<PromotionDTO>>() {
        }.getType());
        PageDTO<PromotionDTO> pageData = new PageDTO<PromotionDTO>(page, pageSize, postEntities.getTotalPages(), promotionDTOList);
        return pageData;
    }

    public PromotionDTO get(int id) throws PostNotExitException {
        Optional<PromotionEntity> postEntity = promotionRepository.findById(id);
        if (!postEntity.isPresent()) {
            throw new PostNotExitException();
        }
        return mapper.map(postEntity.get(), PromotionDTO.class);
    }

    public PromotionDTO create(PostForm postForm) {
        PromotionEntity postEntity = new PromotionEntity();
        mapper.map(postForm, postEntity);
        postEntity.setStatus(PromotionStatus.INIT.getStatus());
        postEntity = promotionRepository.saveAndFlush(postEntity);
        PromotionDTO promotionDTO = new PromotionDTO();
        mapper.map(postEntity, promotionDTO);
        return promotionDTO;
    }

    public boolean publish(int id) throws PostNotExitException {
        Optional<PromotionEntity> postEntity = promotionRepository.findById(id);
        if (!postEntity.isPresent()) {
            throw new PostNotExitException();
        }
        postEntity.get().setStatus(PromotionStatus.PUBLISHED.getStatus());
        promotionRepository.saveAndFlush(postEntity.get());
        return true;
    }

}
