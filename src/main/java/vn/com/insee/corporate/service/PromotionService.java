package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.PromotionStatus;
import vn.com.insee.corporate.common.dto.PromotionUserDTOStatus;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.dto.response.client.PromotionClientDTO;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.PromotionEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FieldNullException;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.ConstructionRepository;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private CustomerRepository customerRepository;

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

    public PromotionClientDTO get(int id, Integer userId) throws PostNotExitException {
        Optional<PromotionEntity> optionalPromotionEntity = promotionRepository.findById(id);
        if (!optionalPromotionEntity.isPresent()) {
            throw new PostNotExitException();
        }
        PromotionEntity promotionEntity = optionalPromotionEntity.get();
        PromotionClientDTO promotionClientDTO = mapper.map(promotionEntity, PromotionClientDTO.class);
        if (userId != null) {
            promotionClientDTO = convertForClient(promotionClientDTO, userId);
        }
        return promotionClientDTO;
    }

    public List<PromotionClientDTO> getList(Integer userId) throws Exception {
        CustomerEntity customerEntity = customerRepository.findByUserId(userId);
        if (customerEntity == null) {
            throw new CustomerExitException();
        }
        Integer mainAreaId = customerEntity.getMainAreaId();
        if (mainAreaId == null) {
            throw new FieldNullException();
        }
        List<PromotionEntity> promotionEntities = promotionRepository.findByLocation(mainAreaId);
        if (promotionEntities == null) {
            return new ArrayList<>();
        }
        List<PromotionClientDTO> rs = new ArrayList<>();
        for (PromotionEntity promotionEntity: promotionEntities) {
            PromotionClientDTO promotionClientDTO = mapper.map(promotionEntity, PromotionClientDTO.class);
            if (userId != null) {
                promotionClientDTO = convertForClient(promotionClientDTO, userId);
            }
            rs.add(promotionClientDTO);
        }
        return rs;
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

    private PromotionClientDTO convertForClient(PromotionClientDTO promotionClientDTO, Integer userId) {
        PromotionUserDTOStatus promotionUserDTOStatus = PromotionUserDTOStatus.CAN_CREATE_NEW;
        List<ConstructionEntity> listPromotionByUser = constructionRepository.findByPromotionIdAndUserId(promotionClientDTO.getId(), userId);
        if (listPromotionByUser != null) {
            for (ConstructionEntity constructionEntity : listPromotionByUser) {
                if (constructionEntity.getStatus() == ConstructionStatus.WAITING_APPROVAL.getStatus()) {
                    promotionUserDTOStatus = PromotionUserDTOStatus.WAITING_APPROVAL;
                    break;
                }
            }
            promotionClientDTO.setListPlayingId(listPromotionByUser.stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
        promotionClientDTO.setPlayingStatus(promotionUserDTOStatus.getStatus());
        return promotionClientDTO;
    }

}
