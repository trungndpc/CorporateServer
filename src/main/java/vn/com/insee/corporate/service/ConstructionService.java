package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.common.MessageManager;
import vn.com.insee.corporate.common.status.ConstructionStatus;
import vn.com.insee.corporate.common.type.TypeConstruction;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.*;
import vn.com.insee.corporate.dto.response.admin.HistoryConstructionDTO;
import vn.com.insee.corporate.dto.response.admin.HistoryConstructionPromotionDTO;
import vn.com.insee.corporate.dto.response.ext.ExtraDTO;
import vn.com.insee.corporate.entity.*;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.*;
import vn.com.insee.corporate.service.external.ZaloService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConstructionService {

    @Autowired
    Mapper mapper;

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BillService billService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    public ConstructionDTO create(ConstructionForm form, int customerId) throws JsonProcessingException {
        ConstructionEntity constructionEntity = mapper.map(form, ConstructionEntity.class);
        constructionEntity.setCustomerId(customerId);
        constructionEntity.setStatus(ConstructionStatus.WAITING_APPROVAL.getStatus());

        ExtraDTO extra = form.getExtra();
        if (extra != null) {
            String s = objectMapper.writeValueAsString(extra);
            constructionEntity.setExtra(s);
        }

        List<String> billIds = form.getBillIds();
        if (billIds != null) {
            constructionEntity.setBillIds(billService.initFromListUrl(billIds));
        }
        List<String> images = form.getImageIds();
        if (images != null) {
            constructionEntity.setImageIds(imageService.initFromListUrl(images));
        }
        constructionEntity = constructionRepository.saveAndFlush(constructionEntity);

        PromotionEntity promotionEntity = promotionRepository.getOne(constructionEntity.getPromotionId());
        String msg = MessageManager.getMsgAfterRegisterPromotion(promotionEntity.getTitle());
        zaloService.sendTxtMsgByCustomerId(customerId, msg);
        return mapper.map(constructionEntity, ConstructionDTO.class);
    }

    public ConstructionDTO update(ConstructionForm form, int customerId) throws NotExitException {
        Optional<ConstructionEntity> optionalConstructionEntity = constructionRepository.findById(form.getId());
        if (!optionalConstructionEntity.isPresent()) {
            throw new NotExitException();
        }

        ConstructionEntity constructionEntity = optionalConstructionEntity.get();
        if (form.getAddress() != null) {
            constructionEntity.setAddress(form.getAddress());
        }
        if (form.getCity() != null) {
            constructionEntity.setCity(form.getCity());
        }
        if (form.getDistrict() != null) {
            constructionEntity.setDistrict(form.getDistrict());
        }
        if (form.getName() != null) {
            constructionEntity.setName(form.getName());
        }
        if (form.getPhone() != null) {
            constructionEntity.setPhone(form.getPhone());
        }
        if (form.getQuantity() != null) {
            constructionEntity.setQuantity(form.getQuantity());
        }
        if (form.getEstimateTimeStart() != null) {
            constructionEntity.setEstimateTimeStart(form.getEstimateTimeStart());
        }
        if (form.getTypeConstruction() != null) {
            constructionEntity.setTypeConstruction(form.getTypeConstruction());
        }

        if (form.getBillIds() != null) {
            List<String> billIds = form.getBillIds();
            if (billIds != null) {
                List<Integer> billIDs = billService.initFromListUrl(billIds);
                List<Integer> constructionEntityBillIds = constructionEntity.getBillIds();
                if (constructionEntityBillIds == null) {
                    constructionEntityBillIds = new ArrayList<>();
                }
                constructionEntityBillIds.addAll(billIDs);
                constructionEntity.setBillIds(constructionEntityBillIds);
            }
        }

        if (form.getImageIds() != null) {
            List<String> images = form.getImageIds();
            if (images != null) {
                List<Integer> imgIds = imageService.initFromListUrl(images);
                List<Integer> constructionEntityImageIds = constructionEntity.getImageIds();
                if (constructionEntityImageIds == null) {
                    constructionEntityImageIds = new ArrayList<>();
                }
                constructionEntityImageIds.addAll(imgIds);
                constructionEntity.setImageIds(constructionEntityImageIds);
            }
        }

        if (form.getCement() != null && form.getCement() != 0) {
            constructionEntity.setCement(form.getCement());
        }
        constructionEntity.setStatus(ConstructionStatus.RE_SUBMIT.getStatus());
        constructionEntity = constructionRepository.saveAndFlush(constructionEntity);
        return mapper.map(constructionEntity, ConstructionDTO.class);
    }

    public ConstructionDTO get(int id) throws ConstructionExitException, JsonProcessingException {
        Optional<ConstructionEntity> constructionEntity = constructionRepository.findById(id);
        if (!constructionEntity.isPresent()) {
            throw new ConstructionExitException();
        }
        ConstructionDTO constructionDTO = mapper.map(constructionEntity.get(), ConstructionDTO.class);
        if (constructionEntity.get().getExtra() != null) {
            ExtraDTO extraDTO = objectMapper.readValue(constructionEntity.get().getExtra(), ExtraDTO.class);
            constructionDTO.setExtra(extraDTO);
        }
        List<Integer> billIds = constructionEntity.get().getBillIds();
        if (billIds != null && billIds.size() > 0) {
            List<BillDTO> billDTOList = billService.getList(billIds);
            if (billDTOList != null) {
                constructionDTO.setBills(billDTOList);
            }
        }
        List<Integer> imageIds = constructionEntity.get().getImageIds();
        if (imageIds != null && imageIds.size() > 0) {
            List<ImageDTO> imageDTOList = imageService.getList(imageIds);
            if (imageDTOList != null) {
                constructionDTO.setImages(imageDTOList);
            }
        }
        Integer labelId = constructionEntity.get().getLabelId();
        if (labelId != null) {
            LabelDTO labelDTO = labelService.get(labelId);
            constructionDTO.setLabel(labelDTO);
        }

        int customerId = constructionEntity.get().getCustomerId();
        CustomerDTO customerDTO = customerService.get(customerId);
        UserEntity userEntity = userRepository.getOne(customerDTO.getUserId());
        constructionDTO.setUser(mapper.map(userEntity, UserDTO.class));
        return constructionDTO;
    }

    public PageDTO<ConstructionDTO> getList(int type, Integer status, int page, int pageSize) {
        Pageable pageable =
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<ConstructionEntity> constructionEntities;
        if (status == null) {
            constructionEntities = constructionRepository.findByType(type, pageable);
        }else {
            constructionEntities = constructionRepository.findByTypeAndStatus(type, status, pageable);
        }
        List<ConstructionDTO> constructionDTOS = new ArrayList<>();
        for (ConstructionEntity constructionEntity: constructionEntities) {
            try {
                ConstructionDTO constructionDTO = mapper.map(constructionEntity, ConstructionDTO.class);
                int customerId = constructionEntity.getCustomerId();
                CustomerDTO customerDTO = customerService.get(customerId);
                Integer userId = customerDTO.getUserId();
                UserEntity userEntity = userRepository.getOne(userId);
                constructionDTO.setUser(mapper.map(userEntity, UserDTO.class));
                constructionDTOS.add(constructionDTO);
            }catch (Exception e) {

            }

        }
        PageDTO<ConstructionDTO> pageData = new PageDTO<ConstructionDTO>(page, pageSize, constructionEntities.getTotalPages(), constructionDTOS);
        return pageData;
    }

    public void updateStatus(int id, ConstructionStatus status, String note) throws ConstructionExitException, JsonProcessingException {
        ConstructionEntity constructionEntity = constructionRepository.getOne(id);

        if (constructionEntity.getStatus() != status.getStatus()) {
            int customerId = constructionEntity.getCustomerId();
            if (status == ConstructionStatus.APPROVED) {
                List<Integer> billIds = constructionEntity.getBillIds();
                if (billIds != null && billIds.size() > 0) {
                    int volumeCiment = billService.countVolumeCiment(billIds);
                    customerService.updateVolumeCiment(customerId, volumeCiment);
                }
            }
            PromotionEntity promotionEntity = promotionRepository.getOne(constructionEntity.getPromotionId());
            if (status == ConstructionStatus.APPROVED) {
                String msg = MessageManager.getMsgApprovedPromotion(promotionEntity.getTitle());
                zaloService.sendTxtMsgByCustomerId(customerId, msg);
            }else if(status == ConstructionStatus.REJECTED) {
                String link = MessageManager.getLink2UpdateConstruction(promotionEntity.getId(), constructionEntity.getId(),
                        TypeConstruction.findByType(constructionEntity.getType()));
                String msg = MessageManager.getMsgRejectedPromotion(promotionEntity.getTitle(), note, link);
                zaloService.sendTxtMsgByCustomerId(customerId, msg);
            }
        }
        constructionEntity.setStatus(status.getStatus());
        if (note != null) {
            constructionEntity.setNote(note);
        }
        constructionRepository.saveAndFlush(constructionEntity);
    }

    public boolean updateLabel(int id, int labelId) throws CustomerExitException {
        Optional<ConstructionEntity> constructionEntityOptional = constructionRepository.findById(id);
        if (!constructionEntityOptional.isPresent()) {
            throw new CustomerExitException();
        }
        ConstructionEntity constructionEntity = constructionEntityOptional.get();
        constructionEntity.setLabelId(labelId);
        constructionRepository.saveAndFlush(constructionEntity);
        return true;
    }

    public void updateGift(int id, int giftId) {
        ConstructionEntity constructionEntity = constructionRepository.getOne(id);
        constructionEntity.setGiftId(giftId);
        constructionEntity.setStatus(ConstructionStatus.SEND_GIFT.getStatus());
        constructionRepository.saveAndFlush(constructionEntity);
    }

    public List<ConstructionDTO> findByCustomerAndPromotionId(int customerId, int promotionId) {
        List<ConstructionEntity> constructionEntityList = constructionRepository.findByCustomerIdAndPromotionId(customerId, promotionId);
        List<ConstructionDTO> constructionDTOS = mapper.mapToList(constructionEntityList, new TypeToken<List<ConstructionDTO>>() {
        }.getType());
        return constructionDTOS;
    }

    public List<HistoryConstructionDTO> getHistoryByCustomerId(int customerId) throws PostNotExitException, JsonProcessingException {
        List<ConstructionEntity> constructionEntities = constructionRepository.findByCustomerId(customerId);
        List<HistoryConstructionDTO> rs = new ArrayList<>();
        for (ConstructionEntity constructionEntity: constructionEntities) {
            HistoryConstructionDTO constructionDTO = mapper.map(constructionEntity, HistoryConstructionDTO.class);
            PromotionDTO promotionDTO = promotionService.get(constructionEntity.getPromotionId());
            constructionDTO.setPromotion(promotionDTO);
            if (constructionEntity.getGiftId() != null) {
                GiftDTO giftDTO = giftService.get(constructionEntity.getGiftId());
                constructionDTO.setGift(giftDTO);
            }
            rs.add(constructionDTO);
        }
        return rs;
    }

    public PageDTO<HistoryConstructionPromotionDTO> getHistoryByPromotionId(int promotionId, int page, int pageSize) {
        Pageable pageable =
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "updatedTime"));
        Page<ConstructionEntity> constructionEntityPage = constructionRepository.findByPromotionId(promotionId, pageable);
        List<HistoryConstructionPromotionDTO> hcPromotionDTOList = new ArrayList<>();
        for (ConstructionEntity constructionEntity: constructionEntityPage) {
            HistoryConstructionPromotionDTO historyConstructionPromotionDTO = mapper.map(constructionEntity, HistoryConstructionPromotionDTO.class);
            CustomerDTO customerDTO = customerService.get(constructionEntity.getCustomerId());
            UserEntity userEntity = userRepository.getOne(customerDTO.getUserId());
            UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
            historyConstructionPromotionDTO.setCustomer(customerDTO);
            historyConstructionPromotionDTO.setUser(userDTO);
            hcPromotionDTOList.add(historyConstructionPromotionDTO);
        }
        return new PageDTO<HistoryConstructionPromotionDTO>(page, pageSize, constructionEntityPage.getTotalPages(), hcPromotionDTOList);
    }


}
