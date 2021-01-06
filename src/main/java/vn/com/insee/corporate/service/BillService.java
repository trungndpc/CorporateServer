package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.entity.BillEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.repository.BillRepository;

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
}
