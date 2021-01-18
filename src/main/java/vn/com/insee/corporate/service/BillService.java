package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.BillStatus;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.dto.response.BillDTO;
import vn.com.insee.corporate.entity.BillEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.BillRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private Mapper mapper;

    public void update(BillDTO billDTO) {
        BillEntity billEntity = billRepository.getOne(billDTO.getId());
        billEntity.setLabelId(billDTO.getLabelId());
        billEntity.setVolumeCiment(billDTO.getVolumeCiment());
        billEntity.setStatus(billDTO.getStatus());
        billRepository.saveAndFlush(billEntity);
    }

    public boolean isLabelExits(String labelId) {
        List<BillEntity> billEntityList = billRepository.findByLabelId(labelId);
        if (billEntityList == null || billEntityList.size() == 0) {
            return false;
        }
        return true;
    }

    public List<Integer> initFromListUrl(List<String> urls) {
        List<BillEntity> billEntities = new ArrayList<>();
        for (String url : urls) {
            BillEntity billEntity = new BillEntity();
            billEntity.setLink(url);
            billEntity.setStatus(BillStatus.WAITING_APPROVAL.getStatus());
            billEntities.add(billEntity);
        }
        billEntities = billRepository.saveAll(billEntities);
        return billEntities.stream().map(b -> b.getId()).collect(Collectors.toList());
    }

    public List<BillDTO> getList(List<Integer> ids) {
        List<BillEntity> entities = billRepository.findAllById(ids);
        if (entities != null) {
            return entities.stream().map(e -> mapper.map(e, BillDTO.class)).collect(Collectors.toList());
        }
        return null;
    }

    public int countVolumeCiment(List<Integer> ids) {
        int volumeCiment = 0;
        List<BillEntity> billEntities = billRepository.findAllById(ids);
        for (BillEntity billEntity: billEntities) {
            if (billEntity.getStatus() == BillStatus.APPROVED.getStatus()) {
                if (billEntity.getVolumeCiment() != null) {
                    volumeCiment = volumeCiment + billEntity.getVolumeCiment();
                }
            }
        }
        return volumeCiment;
    }

}
