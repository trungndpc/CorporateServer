package vn.com.insee.corporate.mapper.dto2entity;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.entity.ConstructionEntity;

public class ConstructionFormToConstructionEntity extends PropertyMap<ConstructionForm, ConstructionEntity> {


    @Override
    protected void configure() {
        skip().setImageIds(null);
        source.setBillIds(null);
    }
}
