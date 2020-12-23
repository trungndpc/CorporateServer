package vn.com.insee.corporate.mapper.dto2entity;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.entity.PostEntity;

public class PostFormToPostEntity  extends PropertyMap<PostForm, PostEntity> {
    @Override
    protected void configure() {
        skip().setId(0);
    }
}