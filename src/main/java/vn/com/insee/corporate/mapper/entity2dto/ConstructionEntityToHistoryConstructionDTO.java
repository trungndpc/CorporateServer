package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.admin.HistoryConstructionDTO;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.mapper.Mapper;

public class ConstructionEntityToHistoryConstructionDTO extends PropertyMap<ConstructionEntity, HistoryConstructionDTO> {

    @Override
    protected void configure() {
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getCreatedTime(), destination.getCreatedTime());
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getUpdatedTime(), destination.getUpdatedTime());
    }
}
