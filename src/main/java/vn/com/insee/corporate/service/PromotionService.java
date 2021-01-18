package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.PromotionStatus;
import vn.com.insee.corporate.common.dto.PromotionUserDTOStatus;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.dto.response.client.PromotionCustomerDTO;
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
    private CustomerService customerService;
    
    @Autowired
    private ConstructionService constructionService;


    @Autowired
    private Mapper mapper;

    public PageDTO<PromotionDTO> getListForAdmin(int page, int pageSize) {
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

    public List<PromotionCustomerDTO> getList(Integer customerId, Integer roleId) throws Exception {
        CustomerDTO customerDTO = customerService.get(customerId);
        if (customerDTO == null) {
            throw new CustomerExitException();
        }
        Integer mainAreaId = customerDTO.getMainAreaId();
        if (mainAreaId == null) {
            throw new FieldNullException();
        }
        List<PromotionEntity> promotionEntities = promotionRepository.findByLocation(mainAreaId);
        if (promotionEntities == null) {
            return new ArrayList<>();
        }
        List<PromotionCustomerDTO> rs = new ArrayList<>();
        for (PromotionEntity promotionEntity: promotionEntities) {
            PromotionCustomerDTO promotionCustomerDTO = convertEntityToPromotionCustomer(promotionEntity, customerId);
            if (roleId != null && roleId == Permission.ADMIN.getId()) {
                rs.add(promotionCustomerDTO);
            }else {
                if (promotionCustomerDTO.getStatus() == PromotionStatus.PUBLISHED.getStatus()) {
                    rs.add(promotionCustomerDTO);
                }
            }
        }
        return rs;
    }

    public int create(PostForm postForm) {
        PromotionEntity postEntity = new PromotionEntity();
        mapper.map(postForm, postEntity);
        postEntity.setStatus(PromotionStatus.INIT.getStatus());
        postEntity = promotionRepository.saveAndFlush(postEntity);
        return postEntity.getId();
    }

    public int update(int id, PostForm postForm) {
        PromotionEntity promotionEntity = promotionRepository.getOne(id);
        if (postForm.getContent() != null) {
            promotionEntity.setContent(postForm.getContent());
        }
        if (postForm.getLocation() != null) {
            promotionEntity.setLocation(postForm.getLocation());
        }
        if (postForm.getTypePromotion() != 0) {
            promotionEntity.setTypePromotion(postForm.getTypePromotion());
        }
        if (postForm.getSummary() != null) {
            promotionEntity.setSummary(postForm.getSummary());
        }
        if (postForm.getTitle() != null) {
            promotionEntity.setTitle(postForm.getTitle());
        }
        if (postForm.getTimeStart() != null) {
            promotionEntity.setTimeStart(postForm.getTimeStart());
        }
        if (postForm.getTimeEnd() != null) {
            promotionEntity.setTimeEnd(postForm.getTimeEnd());
        }
        promotionEntity = promotionRepository.saveAndFlush(promotionEntity);
        return promotionEntity.getId();
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

    private PromotionCustomerDTO convertEntityToPromotionCustomer(PromotionEntity promotionEntity, Integer customerId) {
        PromotionCustomerDTO promotionCustomerDTO = mapper.map(promotionEntity, PromotionCustomerDTO.class);
        if (customerId != null){
            List<ConstructionDTO> constructionDTOList = constructionService.findByCustomerAndPromotionId(customerId, promotionEntity.getId());
            if (constructionDTOList != null) {
                promotionCustomerDTO.setCount(constructionDTOList.size());
            }
        }
        return promotionCustomerDTO;
    }
}
