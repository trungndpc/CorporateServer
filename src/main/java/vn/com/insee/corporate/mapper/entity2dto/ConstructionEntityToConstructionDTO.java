package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.entity.ConstructionEntity;

public class ConstructionEntityToConstructionDTO extends PropertyMap<ConstructionEntity, ConstructionDTO> {
    @Override
    protected void configure() {
        skip().setImages(null);
        skip().setBills(null);
    }
}
