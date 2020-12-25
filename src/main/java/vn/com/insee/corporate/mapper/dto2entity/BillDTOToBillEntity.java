package vn.com.insee.corporate.mapper.dto2entity;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.BillDTO;
import vn.com.insee.corporate.entity.BillEntity;

public class BillDTOToBillEntity extends PropertyMap<BillDTO, BillEntity> {
    @Override
    protected void configure() {

    }
}
