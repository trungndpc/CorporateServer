package vn.com.insee.corporate.mapper.entity2dto;

import org.modelmapper.PropertyMap;
import vn.com.insee.corporate.common.CustomerStatus;
import vn.com.insee.corporate.common.dto.CustomerDTOStatus;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.entity.CustomerEntity;

public class CustomerEntityToCustomerDTO extends PropertyMap<CustomerEntity, CustomerDTO> {

    @Override
    protected void configure() {
        skip().setStatus(CustomerDTOStatus.findBy(CustomerStatus.findByStatus(source.getStatus()), source.getLinkedUser()));
    }
}
