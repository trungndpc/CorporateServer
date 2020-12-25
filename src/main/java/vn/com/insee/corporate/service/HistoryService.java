package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.HistoryGiftStatus;
import vn.com.insee.corporate.dto.HistoryForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.HistoryEntity;
import vn.com.insee.corporate.exception.HistoryEmptyException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.HistoryRepository;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private HistoryRepository historyRepository;

    public HistoryDTO create(HistoryForm form) {
        HistoryEntity historyEntity = new HistoryEntity();
        mapper.map(form, historyEntity);
        historyEntity.setStatus(HistoryGiftStatus.SEND.getStatus());
        historyEntity = historyRepository.saveAndFlush(historyEntity);
        HistoryDTO historyDTO = new HistoryDTO();
        mapper.map(historyEntity, historyDTO);
        return historyDTO;
    }

    public List<HistoryDTO> findByUserId(int userId) throws HistoryEmptyException {
        List<HistoryEntity> historyEntities = historyRepository.findByUserId(userId);
        if (historyEntities == null) {
            throw new HistoryEmptyException();
        }
        List<HistoryDTO> historyDTOS = mapper.mapToList(historyEntities, new TypeToken<List<HistoryDTO>>() {
            }.getType());
        return historyDTOS;
    }
}
