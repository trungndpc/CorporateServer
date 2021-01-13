package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.GiftStatus;
import vn.com.insee.corporate.dto.GiftForm;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.entity.GiftEntity;
import vn.com.insee.corporate.entity.HistoryEntity;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.GiftRepository;

import java.util.List;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ConstructionService constructionService;

    public GiftDTO create(GiftForm giftForm) throws ConstructionExitException, JsonProcessingException {
        GiftEntity giftEntity = mapper.map(giftForm, GiftEntity.class);
        giftEntity.setStatus(GiftStatus.SEND.getStatus());
        giftEntity = giftRepository.saveAndFlush(giftEntity);
        constructionService.updateStatus(giftEntity.getConstructionId(), ConstructionStatus.SEND_GIFT);
        return mapper.map(giftEntity, GiftDTO.class);
    }

    public List<GiftDTO> getListByUid(int uid) {
        List<GiftEntity> giftEntities = giftRepository.findByUserId(uid);
        List<GiftDTO> giftDTOS = mapper.mapToList(giftEntities, new TypeToken<List<GiftDTO>>() {
        }.getType());
        return giftDTOS;
    }

}
