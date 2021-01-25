package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.MessageManager;
import vn.com.insee.corporate.common.status.ConstructionStatus;
import vn.com.insee.corporate.common.status.GiftStatus;
import vn.com.insee.corporate.common.type.TypeGift;
import vn.com.insee.corporate.dto.GiftForm;
import vn.com.insee.corporate.dto.response.*;
import vn.com.insee.corporate.dto.response.admin.HistoryGiftDTO;
import vn.com.insee.corporate.dto.response.client.HistoryGiftCustomerDTO;
import vn.com.insee.corporate.dto.response.ext.PhoneCard;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.GiftEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.exception.NotPermissionException;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.GiftRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.ZaloService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private CustomerRepository customerRepository;

    public GiftDTO create(GiftForm giftForm) throws ConstructionExitException, JsonProcessingException {
        GiftEntity giftEntity = convertFormToEntity(giftForm);
        giftEntity.setStatus(GiftStatus.SEND.getStatus());
        giftEntity = giftRepository.saveAndFlush(giftEntity);
        constructionService.updateGift(giftEntity.getConstructionId(), giftEntity.getId());

        CustomerEntity customerEntity = customerRepository.getOne(giftEntity.getCustomerId());
        String title = "Chúc mừng !!!";
        String subTitle = MessageManager.getMsgSendGiftPromotion(customerEntity.getFullName(), giftEntity.getName());
        String imageUrl = "https://trungndpc.github.io/insee-promotion-client/images/banner.png";
        String link = "https://insee-client.wash-up.vn/chuc-mung/" + giftEntity.getId();
        zaloService.sendActionList(customerEntity.getUserId(), imageUrl, link, title, subTitle);
        return mapper.map(giftEntity, GiftDTO.class);
    }

    public  List<HistoryGiftCustomerDTO> getHistoryByCustomerId(int customerId) throws Exception {
        List<GiftEntity> giftEntities = giftRepository.findByCustomerId(customerId);
        List<HistoryGiftCustomerDTO> historyGiftDTOS = new ArrayList<>();
        for (GiftEntity giftEntity: giftEntities) {
            historyGiftDTOS.add(convertEntityToGiftDTO(giftEntity));
        }
        return historyGiftDTOS;
    }

    public List<HistoryGiftDTO> getHistory() throws ConstructionExitException, PostNotExitException, JsonProcessingException {
        List<GiftEntity> giftRepositoryAll = giftRepository.findAll();
        List<HistoryGiftDTO> rs = new ArrayList<>();
        for (GiftEntity giftEntity: giftRepositoryAll) {
            try {
                HistoryGiftDTO historyGiftDTO = convertEntityToGiftCustomerDTO(giftEntity);
                rs.add(historyGiftDTO);
            }catch (Exception e) {

            }
        }
        return rs;
    }

    public GiftDTO get(int id) throws JsonProcessingException {
        GiftEntity giftEntity = giftRepository.getOne(id);
        return convertEntityToDTO(giftEntity);
    }

    public void received(int id, int customerId) throws NotExitException, ConstructionExitException, JsonProcessingException, NotPermissionException {
        GiftEntity one = giftRepository.getOne(id);
        if (one == null) {
            throw new NotExitException();
        }
        if (one.getCustomerId() != customerId) throw new NotPermissionException("");
        one.setStatus(GiftStatus.RECEIVED.getStatus());
        giftRepository.saveAndFlush(one);
        constructionService.updateStatus(one.getConstructionId(), ConstructionStatus.RECEIVED_GIFT, null);
    }

    private GiftEntity convertFormToEntity(GiftForm giftForm) throws JsonProcessingException {
        GiftEntity giftEntity = new GiftEntity();
        mapper.map(giftForm, giftEntity);
        TypeGift typeGift = TypeGift.findByValue(giftForm.getType());
        if (typeGift == TypeGift.CARD_PHONE) {
            List<PhoneCard> cards = giftForm.getCards();
            String value = objectMapper.writeValueAsString(cards);
            giftEntity.setData(value);
        }
        return giftEntity;
    }

    private HistoryGiftCustomerDTO convertEntityToGiftDTO(GiftEntity giftEntity) throws ConstructionExitException, PostNotExitException, JsonProcessingException {
        HistoryGiftCustomerDTO historyGiftDTO = mapper.map(giftEntity, HistoryGiftCustomerDTO.class);
        ConstructionDTO constructionDTO = constructionService.get(giftEntity.getConstructionId());
        historyGiftDTO.setConstruction(constructionDTO);
        PromotionDTO promotionDTO = promotionService.get(constructionDTO.getPromotionId());
        historyGiftDTO.setPromotion(promotionDTO);
        return historyGiftDTO;
    }

    private HistoryGiftDTO convertEntityToGiftCustomerDTO(GiftEntity giftEntity) throws ConstructionExitException, PostNotExitException, JsonProcessingException {
        HistoryGiftDTO giftCustomerDTO = mapper.map(giftEntity, HistoryGiftDTO.class);
        ConstructionDTO constructionDTO = constructionService.get(giftEntity.getConstructionId());
        PromotionDTO promotionDTO = promotionService.get(constructionDTO.getPromotionId());
        CustomerDTO customerDTO = customerService.get(giftEntity.getCustomerId());
        giftCustomerDTO.setConstruction(constructionDTO);
        giftCustomerDTO.setPromotion(promotionDTO);
        giftCustomerDTO.setCustomer(customerDTO);
        return giftCustomerDTO;
    }

    private GiftDTO convertEntityToDTO(GiftEntity giftEntity) throws JsonProcessingException {
        GiftDTO giftDTO = mapper.map(giftEntity, GiftDTO.class);
        TypeGift typeGift = TypeGift.findByValue(giftEntity.getType());
        if (typeGift == TypeGift.CARD_PHONE) {
            String data = giftEntity.getData();
            if (data != null) {
                List<PhoneCard> phoneCards = objectMapper.readValue(data, new TypeReference<List<PhoneCard>>() {
                });
                System.out.println(phoneCards);
                giftDTO.setCards(phoneCards);
            }
        }
        return giftDTO;
    }

}
