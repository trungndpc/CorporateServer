package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.mapper.Mapper;

public class ConstructionEntityToConstructionDTO extends PropertyMap<ConstructionEntity, ConstructionDTO> {
    @Override
    protected void configure() {
        skip().setImages(null);
        skip().setBills(null);
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getCreatedTime(), destination.getCreatedTime());
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getUpdatedTime(), destination.getUpdatedTime());
    }
}
