package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.*;
import vn.com.insee.corporate.dto.response.admin.HistoryConstructionDTO;
import vn.com.insee.corporate.dto.response.ext.ExtraDTO;
import vn.com.insee.corporate.entity.*;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.*;
import vn.com.insee.corporate.service.external.ZaloService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public void create(ConstructionForm form, int customerId) throws JsonProcessingException {
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
            ConstructionDTO constructionDTO = mapper.map(constructionEntity, ConstructionDTO.class);
            int customerId = constructionEntity.getCustomerId();
            CustomerDTO customerDTO = customerService.get(customerId);
            Integer userId = customerDTO.getUserId();
            UserEntity userEntity = userRepository.getOne(userId);
            constructionDTO.setUser(mapper.map(userEntity, UserDTO.class));
            constructionDTOS.add(constructionDTO);
        }
        PageDTO<ConstructionDTO> pageData = new PageDTO<ConstructionDTO>(page, pageSize, constructionEntities.getTotalPages(), constructionDTOS);
        return pageData;
    }

    public void updateStatus(int id, ConstructionStatus status) throws ConstructionExitException, JsonProcessingException {
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
            int userId = customerService.get(customerId).getUserId();
            UserEntity userEntity = userRepository.getOne(userId);
            if (userEntity != null && userEntity.getFollowerZaloId() != null)
            if (status == ConstructionStatus.APPROVED) {
                zaloService.sendTextMsg(userEntity.getFollowerZaloId(), "Công trình của bạn đã được chúng tôi xác thực! Trạng thái hóa đơn, hình ảnh của bạn upload sẽ được chúng tôi cập nhật trên trang nhà thầu chính thức của INSEE.");
            }else if(status == ConstructionStatus.REJECTED) {
                zaloService.sendTextMsg(userEntity.getFollowerZaloId(), "Rất tiếc!!! Những thông tin công trình của bạn cung cấp không đáp ứng được yêu cầu của chúng tôi!!!");
            }
        }
        constructionEntity.setStatus(status.getStatus());
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


}
