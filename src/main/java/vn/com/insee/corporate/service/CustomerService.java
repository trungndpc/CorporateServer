package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.CustomerStatus;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Mapper mapper;

    public CustomerDTO get(int id) {
        CustomerEntity one = customerRepository.getOne(id);
        CustomerDTO dto = new CustomerDTO();
        mapper.map(one, dto);
        return dto;
    }

    public CustomerDTO findByPhone(String phone) {
        CustomerEntity customerEntity = customerRepository.findByPhone(phone);
        if (customerEntity != null) {
            CustomerDTO dto = new CustomerDTO();
            mapper.map(customerEntity, dto);
            return dto;
        }
        return null;
    }


    //Client
    public void linkCustomerToUserId(Integer customerID, Integer userId) {
        CustomerEntity customerEntity = customerRepository.getOne(customerID);
        if (customerEntity != null) {
            customerEntity.setUserId(userId);
            customerEntity.setLinkedUser(true);
            customerRepository.saveAndFlush(customerEntity);
        }
    }

    public CustomerDTO createOrUpdate(RegisterForm form, Integer userId) throws CustomerExitException, FirebaseAuthenException {
        UserEntity userEntity = userRepository.getOne(userId);
        Integer customerId = userEntity.getCustomerId();
        if (customerId != null) {
            Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(customerId);
            if (optionalCustomerEntity.isPresent()) {
                CustomerEntity customerEntity = optionalCustomerEntity.get();
                String phone = customerEntity.getPhone();
                mapper.map(form, customerEntity);
                customerEntity.setId(customerId);
                customerEntity.setUserId(userId);
                customerEntity.setPhone(phone);
                customerEntity.setStatus(CustomerStatus.REVIEWING.getStatus());
                customerEntity.setLinkedUser(true);
                customerRepository.saveAndFlush(customerEntity);
                CustomerDTO customerDTO = new CustomerDTO();
                mapper.map(customerEntity, customerDTO);
                return customerDTO;
            }
        }else {
            CustomerEntity customerEntity = new CustomerEntity();
            mapper.map(form, customerEntity);
            customerEntity.setUserId(userId);
            customerEntity.setStatus(CustomerStatus.REVIEWING.getStatus());
            customerEntity.setLinkedUser(true);
            customerEntity.setPhone(userEntity.getPhone());
            customerEntity = customerRepository.saveAndFlush(customerEntity);
            CustomerDTO customerDTO = new CustomerDTO();
            mapper.map(customerEntity, customerDTO);
            return customerDTO;
        }
        return null;
    }


    //Admin
    public CustomerDTO createByAdmin(RegisterForm form) {
        CustomerEntity customerEntity = new CustomerEntity();
        mapper.map(form, customerEntity);
        customerEntity.setStatus(CustomerStatus.APPROVED.getStatus());
        customerEntity.setLinkedUser(false);
        customerEntity = customerRepository.saveAndFlush(customerEntity);
        CustomerDTO customerDTO = new CustomerDTO();
        mapper.map(customerEntity, customerDTO);
        return customerDTO;
    }

    public PageDTO<CustomerDTO> getList(int page, int pageSize) {
        Page<CustomerEntity> customerEntities = customerRepository.findAll(PageRequest.of(page, pageSize));
        List<CustomerDTO> customerDTOS = mapper.mapToList(customerEntities.toList(), new TypeToken<List<CustomerDTO>>() {
        }.getType());
        PageDTO<CustomerDTO> pageData = new PageDTO<CustomerDTO>(page, pageSize, customerEntities.getTotalPages(), customerDTOS);
        return pageData;
    }

    public CustomerDTO updateStatus(int id, CustomerStatus statusEnum) throws CustomerExitException {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(id);
        if (!optionalCustomerEntity.isPresent()) {
            throw new CustomerExitException();
        }
        optionalCustomerEntity.get().setStatus(statusEnum.getStatus());
        CustomerEntity customerEntity = customerRepository.saveAndFlush(optionalCustomerEntity.get());
        CustomerDTO customerDTO = new CustomerDTO();
        mapper.map(customerEntity, customerDTO);
        return customerDTO;
    }

    public void delete(int id) {
        customerRepository.deleteById(id);
    }

}
