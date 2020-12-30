package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.security.InseeUserDetail;
import vn.com.insee.corporate.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("")
public class PingController {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository repository;

    @GetMapping("/ping")
    String all(Authentication authentication) {
        List<CustomerEntity> all = customerRepository.findAll();
        for (CustomerEntity e:
             all) {
            if (e.getId() != 1025) {
                e.setLinkedUser(false);
                customerRepository.save(e);
            }
        }
        return "OK";
    }

    protected void genCustomer() {
        List<CustomerEntity> all = customerRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            CustomerEntity customerEntity = all.get(i);
            for (int j = 10; j < 99; j++) {
                CustomerEntity clone = new CustomerEntity();
                clone.setFullName(customerEntity.getFullName() + " " + j);
                clone.setPhone("09727971" + j);
                clone.setAvatar(customerEntity.getAvatar());
                clone.setMainAreaId(1);
                clone.setStatus(customerEntity.getStatus());
                customerRepository.save(clone);
            }
        }
    }
}
