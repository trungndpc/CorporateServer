package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.repository.CustomerRepository;

import java.util.List;

@RestController
@RequestMapping("")
public class PingController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/ping")
    String all() {
        List<CustomerEntity> all = customerRepository.findAll();
        System.out.println(all.size());
        return "OK";
    }
}
