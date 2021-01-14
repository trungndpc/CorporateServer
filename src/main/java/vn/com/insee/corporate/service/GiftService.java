package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.GiftStatus;
import vn.com.insee.corporate.dto.GiftForm;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.dto.response.client.gift.HistoryGiftConstructionDTO;
import vn.com.insee.corporate.dto.response.client.gift.HistoryGiftDTO;
import vn.com.insee.corporate.dto.response.client.gift.HistoryGiftPromotionDTO;
import vn.com.insee.corporate.entity.GiftEntity;
import vn.com.insee.corporate.entity.HistoryEntity;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.GiftRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private PromotionService promotionService;

    public GiftDTO create(GiftForm giftForm) throws ConstructionExitException, JsonProcessingException {
        GiftEntity giftEntity = mapper.map(giftForm, GiftEntity.class);
        giftEntity.setStatus(GiftStatus.SEND.getStatus());
        giftEntity = giftRepository.saveAndFlush(giftEntity);
        constructionService.updateStatus(giftEntity.getConstructionId(), ConstructionStatus.SEND_GIFT);
        return mapper.map(giftEntity, GiftDTO.class);
    }

    public  List<HistoryGiftDTO> getListByUid(int uid) throws Exception {
        List<GiftEntity> giftEntities = giftRepository.findByUserId(uid);
        List<HistoryGiftDTO> historyGiftDTOS = new ArrayList<>();
        for (GiftEntity giftEntity: giftEntities) {
            HistoryGiftDTO historyGiftDTO = mapper.map(giftEntity, HistoryGiftDTO.class);
            ConstructionDTO constructionDTO = constructionService.findById(giftEntity.getConstructionId());
            HistoryGiftConstructionDTO historyGiftConstructionDTO = mapper.map(constructionDTO, HistoryGiftConstructionDTO.class);
            historyGiftDTO.setConstruction(historyGiftConstructionDTO);
            PromotionDTO promotionDTO = promotionService.get(constructionDTO.getPromotionId());
            HistoryGiftPromotionDTO historyGiftPromotionDTO = mapper.map(promotionDTO, HistoryGiftPromotionDTO.class);
            historyGiftDTO.setPromotion(historyGiftPromotionDTO);
            historyGiftDTOS.add(historyGiftDTO);
        }
        return historyGiftDTOS;
    }

    public GiftDTO getById(int id) {
        GiftEntity giftEntity = giftRepository.getOne(id);
        return mapper.map(giftEntity, GiftDTO.class);
    }

}
