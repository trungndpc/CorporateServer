package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.common.status.PromotionStatus;
import vn.com.insee.corporate.dto.PromotionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.dto.response.admin.report.PromotionReportDTO;
import vn.com.insee.corporate.dto.response.client.PromotionCustomerDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.PromotionEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.NeedToApprovalException;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.ConstructionRepository;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ConstructionService constructionService;
    
    @Autowired
    private ConstructionRepository constructionRepository;


    @Autowired
    private Mapper mapper;

    public PageDTO<PromotionDTO> getListForAdmin(int page, int pageSize) {
        Page<PromotionEntity> postEntities = promotionRepository.findAll(PageRequest.of(page, pageSize));
        List<PromotionDTO> promotionDTOList = new ArrayList<>();
        for (PromotionEntity promotionEntity: postEntities) {
            PromotionDTO promotionDTO = mapper.map(promotionEntity, PromotionDTO.class);
            long countByPromotionId = constructionRepository.countByPromotionId(promotionDTO.getId());
            PromotionReportDTO promotionReportDTO = new PromotionReportDTO();
            promotionReportDTO.setNumberOfParticipants(countByPromotionId);
            promotionDTO.setReport(promotionReportDTO);
            promotionDTOList.add(promotionDTO);
        }
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

    public List<PromotionCustomerDTO> getList(Integer customerId, Integer roleId) throws CustomerExitException, NeedToApprovalException {
        CustomerEntity customerEntity = customerRepository.getOne(customerId);
        if (customerEntity == null) {
            throw new CustomerExitException();
        }

        if (customerEntity.getStatus() != CustomerStatus.APPROVED.getStatus()) {
            throw new NeedToApprovalException();
        }

        List<PromotionEntity> promotionEntities = promotionRepository.findByLocation(customerEntity.getMainAreaId());
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

    public int create(PromotionForm promotionForm) {
        PromotionEntity postEntity = new PromotionEntity();
        mapper.map(promotionForm, postEntity);
        postEntity.setStatus(PromotionStatus.INIT.getStatus());
        postEntity = promotionRepository.saveAndFlush(postEntity);
        return postEntity.getId();
    }

    public int update(int id, PromotionForm promotionForm) {
        PromotionEntity promotionEntity = promotionRepository.getOne(id);
        if (promotionForm.getContent() != null) {
            promotionEntity.setContent(promotionForm.getContent());
        }
        if (promotionForm.getLocation() != null) {
            promotionEntity.setLocation(promotionForm.getLocation());
        }
        if (promotionForm.getTypePromotion() != 0) {
            promotionEntity.setTypePromotion(promotionForm.getTypePromotion());
        }
        if (promotionForm.getSummary() != null) {
            promotionEntity.setSummary(promotionForm.getSummary());
        }
        if (promotionForm.getTitle() != null) {
            promotionEntity.setTitle(promotionForm.getTitle());
        }
        if (promotionForm.getTimeStart() != null) {
            promotionEntity.setTimeStart(promotionForm.getTimeStart());
        }
        if (promotionForm.getTimeEnd() != null) {
            promotionEntity.setTimeEnd(promotionForm.getTimeEnd());
        }

        if (promotionForm.getCover() != null) {
            promotionEntity.setCover(promotionForm.getCover());
        }

        if (promotionForm.getRuleAcceptedCement() != null) {
            promotionEntity.setRuleAcceptedCement(promotionForm.getRuleAcceptedCement());
        }

        if (promotionForm.getRuleQuantily() != null) {
            promotionEntity.setRuleQuantily(promotionForm.getRuleQuantily());
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
