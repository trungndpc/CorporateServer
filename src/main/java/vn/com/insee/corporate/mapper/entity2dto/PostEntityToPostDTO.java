package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.PostDTO;
import vn.com.insee.corporate.entity.PostEntity;

public class PostEntityToPostDTO extends PropertyMap<PostEntity, PostDTO> {
    @Override
    protected void configure() {

    }
}