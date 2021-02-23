package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.client.HistoryGiftCustomerDTO;
import vn.com.insee.corporate.entity.GiftEntity;
import vn.com.insee.corporate.mapper.Mapper;

public class GiftEntityToHistoryGiftCustomerDTO extends PropertyMap<GiftEntity, HistoryGiftCustomerDTO> {
    @Override
    protected void configure() {
        using(Mapper.ZONE_DATE_TIME_2_LONG).map(source.getUpdatedTime(), destination.getUpdatedTime());
    }
}
