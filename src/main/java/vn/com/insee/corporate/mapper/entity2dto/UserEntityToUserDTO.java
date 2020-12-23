package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;

public class UserEntityToUserDTO extends PropertyMap<UserEntity, UserDTO> {

    @Override
    protected void configure() {

    }
}
