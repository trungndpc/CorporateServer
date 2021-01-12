package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.entity.BillEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.repository.BillRepository;

import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public void updateStatus(int id, ImageStatus imageStatus) throws NotExitException {
        BillEntity billEntity = billRepository.getOne(id);
        if (billEntity == null) throw new NotExitException();
        billEntity.setStatus(imageStatus.getStatus());
        billRepository.save(billEntity);
    }

    public void updateVolumeCiment(int id, int volumeCiment) throws NotExitException {
        BillEntity billEntity = billRepository.getOne(id);
        if (billEntity == null) throw new NotExitException();
        billEntity.setVolumeCiment(volumeCiment);
    }

    public boolean isExits(String labelId) {
        List<BillEntity> billEntityList = billRepository.findByLabelId(labelId);
        if (billEntityList == null || billEntityList.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean updateLabelId(int id, String labelId) throws NotExitException {
        if (isExits(labelId)) {
            return false;
        }
        BillEntity billEntity = billRepository.getOne(id);
        if (billEntity == null) throw new NotExitException();
        billEntity.setLabelId(labelId);
        billRepository.saveAndFlush(billEntity);
        return true;
    }

}
