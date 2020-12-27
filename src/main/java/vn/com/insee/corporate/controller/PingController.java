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
        customerRepository.deleteAll();
        repository.deleteAll();
        return "OK";
    }
}
