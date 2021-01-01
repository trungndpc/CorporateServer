package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.Permission;
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

    @GetMapping("/delete-all")
    String all(Authentication authentication) {
        repository.deleteAll();
        customerRepository.deleteAll();
        return "OK";
    }

    @GetMapping("/add-admin")
    String addAdmin(Authentication authentication) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Nguyễn Công Phượng");
        userEntity.setAvatar("https://static2.yan.vn/YanNews/202006/202006030349344511-2fc01591-f2a8-45f8-a210-79f517c9297b.png");
        userEntity.setPassword("1");
        userEntity.setPassword("84972797200");
        userEntity.setEnable(true);
        userEntity.setStatus(1);
        userEntity.setRoleId(Permission.ADMIN.getId());
        repository.saveAndFlush(userEntity);
        return "OK";
    }

    @GetMapping("/auto-gen")
    String autoGen(Authentication authentication) {
        genCustomer();
        return "OK";
    }



    protected void genCustomer() {
        List<CustomerEntity> all = customerRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            CustomerEntity customerEntity = all.get(i);
            for (int j = 10; j < 99; j++) {
                CustomerEntity clone = new CustomerEntity();
                clone.setFullName("Auto gen" + " " + j);
                clone.setPhone("09227971" + j);
                clone.setAvatar(customerEntity.getAvatar());
                clone.setMainAreaId(1);
                clone.setStatus(customerEntity.getStatus());
                customerRepository.save(clone);
            }
        }
    }
}
