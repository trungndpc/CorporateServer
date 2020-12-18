package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("")
public class PingController {


    @Autowired
    private CustomerService customerService;

    @GetMapping("/ping")
    String all() {
        customerService.v();
        return "OK";
    }
}
