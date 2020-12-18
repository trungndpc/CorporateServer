package vn.com.insee.corporate.mapper.dto2entity;

import org.modelmapper.Conditions;
import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.entity.CustomerEntity;

public class CustomerFormToCustomerEntity extends PropertyMap<RegisterForm, CustomerEntity> {

    @Override
    protected void configure() {
    }
}
