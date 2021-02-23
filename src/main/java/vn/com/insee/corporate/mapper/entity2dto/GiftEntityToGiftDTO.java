package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.entity.GiftEntity;
import vn.com.insee.corporate.entity.PromotionEntity;
import vn.com.insee.corporate.mapper.Mapper;

public class GiftEntityToGiftDTO extends PropertyMap<GiftEntity, GiftDTO> {
    @Override
    protected void configure() {
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getCreatedTime(), destination.getCreatedTime());
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getUpdatedTime(), destination.getUpdatedTime());
    }
}
