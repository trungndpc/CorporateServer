package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.entity.PromotionEntity;

public class PostEntityToPostDTO extends PropertyMap<PromotionEntity, PromotionDTO> {
    @Override
    protected void configure() {

    }
}
