package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.MessageManager;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.common.dto.CustomerDTOStatus;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.exception.ParamNotSupportException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.repository.custom.metrics.CustomerLocationMetric;
import vn.com.insee.corporate.service.external.ZaloService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private GiftService giftService;
    
    @Autowired
    private Mapper mapper;

    public CustomerDTO get(int id) {
        CustomerEntity one = customerRepository.getOne(id);
        CustomerDTO dto = new CustomerDTO();
        mapper.map(one, dto);
        CustomerDTOStatus customerDTOStatus = CustomerDTOStatus.findBy(CustomerStatus.findByStatus(one.getStatus()), one.getLinkedUser());
        dto.setFinalStatus(customerDTOStatus.getStatus());
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
            customerEntity.setAvatar(userEntity.getAvatar());
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

    public CustomerDTO updateStatus(int id, CustomerStatus statusEnum, String note) throws CustomerExitException, JsonProcessingException {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(id);
        if (!optionalCustomerEntity.isPresent()) {
            throw new CustomerExitException();
        }
        Integer userId = optionalCustomerEntity.get().getUserId();
        if (userId != null) {
            int currentStatus = optionalCustomerEntity.get().getStatus();
            if (currentStatus != statusEnum.getStatus()) {
                if (statusEnum.equals(CustomerStatus.APPROVED)) {
                    String msg = MessageManager.getMsgRegisterSuccessful(optionalCustomerEntity.get().getFullName());
                    zaloService.sendTxtMsg(userId, msg);
                }else if (statusEnum.equals(CustomerStatus.REJECTED)) {
                    String msg = MessageManager.getMsgRegisterFailed(note);
                    zaloService.sendTxtMsg(userId, msg);
                }
            }
        }
        optionalCustomerEntity.get().setStatus(statusEnum.getStatus());
        optionalCustomerEntity.get().setNote(note);
        CustomerEntity customerEntity = customerRepository.saveAndFlush(optionalCustomerEntity.get());
        CustomerDTO customerDTO = new CustomerDTO();
        mapper.map(customerEntity, customerDTO);
        return customerDTO;
    }

    public PageDTO<CustomerDTO> findAll(int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<CustomerEntity> customerEntityPage = customerRepository.findAll(pageable);
        List<CustomerDTO> customerDTOS = convert(customerEntityPage.toList());
        PageDTO<CustomerDTO> customerDTOPage = new PageDTO<CustomerDTO>(page, size, customerEntityPage.getTotalPages(), customerDTOS);
        return customerDTOPage;
    }

    public PageDTO<CustomerDTO> findBy(CustomerDTOStatus dtoStatus , Integer location, int page, int size) throws ParamNotSupportException {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<CustomerEntity> customerEntityPage = null;
        if (dtoStatus == null) {
            customerEntityPage = customerRepository.getListByStatusAndLocationAndLinkedUser(null,  location, true, pageable);
        }else if (dtoStatus.equals(CustomerDTOStatus.DO_NOT_HAVE_ACCOUNT)) {
            customerEntityPage = customerRepository.getListByStatusAndLocationAndLinkedUser(null,  location, false, pageable);
        }else if (dtoStatus.equals(CustomerDTOStatus.NEED_REVIEW)) {
            customerEntityPage = customerRepository.getListByStatusAndLocationAndLinkedUser(CustomerStatus.REVIEWING.getStatus(), location,true, pageable);
        }else if (dtoStatus.equals(CustomerDTOStatus.APPROVED)) {
            customerEntityPage = customerRepository.getListByStatusAndLocationAndLinkedUser(CustomerStatus.APPROVED.getStatus(), location, true, pageable);
        }else if (dtoStatus.equals(CustomerDTOStatus.REJECTED)) {
            customerEntityPage = customerRepository.getListByStatusAndLocationAndLinkedUser(CustomerStatus.REJECTED.getStatus(), location, true, pageable);
        }
        if (customerEntityPage == null) {
            throw new ParamNotSupportException();
        }
        List<CustomerDTO> customerDTOS = convert(customerEntityPage.toList());
        PageDTO<CustomerDTO> customerDTOPage = new PageDTO<CustomerDTO>(page, size, customerEntityPage.getTotalPages(), customerDTOS);
        return customerDTOPage;
    }

    private List<CustomerDTO> convert(List<CustomerEntity> customerEntities) {
        if (customerEntities == null) {
            return null;
        }
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (CustomerEntity customer: customerEntities) {
            CustomerDTO customerDTO = new CustomerDTO();
            mapper.map(customer, customerDTO);
            CustomerDTOStatus customerDTOStatus = CustomerDTOStatus.findBy(CustomerStatus.findByStatus(customer.getStatus()),
                    customer.getLinkedUser() == null ? false : customer.getLinkedUser());
            customerDTO.setFinalStatus(customerDTOStatus.getStatus());
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public void delete(int id) {
        customerRepository.deleteById(id);
    }

    public void updateVolumeCiment(int customerId, int volume) {
        CustomerEntity customerEntity = customerRepository.getOne(customerId);
        Integer volumeCiment = customerEntity.getVolumeCiment();
        if (volumeCiment == null) {
            volumeCiment = 0;
        }
        customerEntity.setVolumeCiment(volumeCiment + volume);
        customerRepository.saveAndFlush(customerEntity);
    }

}
