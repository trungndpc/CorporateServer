package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.StatusEnum;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.service.external.FirebaseService;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private Mapper mapper;

    public CustomerDTO get(int id) {
        CustomerEntity one = customerRepository.getOne(id);
        CustomerDTO dto = new CustomerDTO();
        mapper.map(one, dto);
        return dto;
    }

    public CustomerDTO register(RegisterForm form) throws CustomerExitException, FirebaseAuthenException {
        if (customerRepository.findByPhone(form.getPhone()) != null) {
            throw new CustomerExitException();
        }

        String idToken = form.getIdToken();
        String firebaseUID = firebaseService.verifyTokenId(idToken);
        String phoneToken = firebaseService.getUserPhoneNumberByUid(firebaseUID);
        phoneToken = phoneToken.replace("+", "");
        if (!form.getPhone().equals(phoneToken)) {
            throw new FirebaseAuthenException(FirebaseAuthenException.FirebaseAuthenError.AUTH_ERROR);
        }

        CustomerEntity customerEntity = new CustomerEntity();
        mapper.map(form, customerEntity);
        customerEntity.setStatus(StatusEnum.INIT.getStatus());
        customerEntity = customerRepository.saveAndFlush(customerEntity);
        CustomerDTO resp = new CustomerDTO();
        mapper.map(customerEntity, resp);
        return resp;
    }

    public boolean isPhoneExits(String phone) {
        return customerRepository.findByPhone(phone) != null;
    }

    public String verifySMSCode(String token, String smsCode) {
        return "";
    }

    public PageDTO<CustomerDTO> getList(int page, int pageSize) {
        Page<CustomerEntity> customerEntities = customerRepository.findAll(PageRequest.of(page, pageSize));
        List<CustomerDTO> customerDTOS = mapper.mapToList(customerEntities.toList(), new TypeToken<List<CustomerDTO>>() {
        }.getType());
        PageDTO<CustomerDTO> pageData = new PageDTO<CustomerDTO>(page, pageSize, customerEntities.getTotalPages(), customerDTOS);
        return pageData;
    }

    public void delete(int id) {
        customerRepository.deleteById(id);
    }

    public void v() {
        List<CustomerEntity> all = customerRepository.findAll();
        for (CustomerEntity entity: all) {
            entity.setPhone(entity.getPhone().replace("+", ""));
            customerRepository.saveAndFlush(entity);
        }
    }
}
