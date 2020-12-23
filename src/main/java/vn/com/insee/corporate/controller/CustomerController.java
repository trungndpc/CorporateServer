package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.CustomerService;
import vn.com.insee.corporate.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/", produces = {"application/json"})
    public ResponseEntity<BaseResponse> get(@RequestParam(required = true) int id) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            CustomerDTO customerDTO = service.get(id);
            if (customerDTO == null) {
                response.setError(ErrorCode.NOT_EXITS);
            }else{
                response.setData(customerDTO);
            }
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(@RequestParam (required = false, defaultValue = "0") int page,
                                                       @RequestParam (required = false, defaultValue = "20") int pageSize) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PageDTO<CustomerDTO> list = service.getList(page, pageSize);
            response.setData(list);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/check-phone", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody Map<String, String> dataMap)  {
        String phone = dataMap.get("phone");
        phone = phone.replace("+", "");
        boolean isPhoneExits = service.isPhoneExits(phone);
        BaseResponse response = new BaseResponse(isPhoneExits ? ErrorCode.PHONE_EXITS : ErrorCode.SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse();
        try{
            CustomerDTO customerDTO = service.register(form);
            if (customerDTO != null) {
                authentication.getPrincipal();
                response.setError(ErrorCode.SUCCESS);
                response.setData(customerDTO);
            }else{
                response.setError(ErrorCode.FAILED);
            }
        }catch (CustomerExitException e) {
            response.setError(ErrorCode.PHONE_EXITS);
        }catch (FirebaseAuthenException e) {
            response.setError(ErrorCode.VERIFY_FAILED);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/delete", produces = {"application/json"})
    public ResponseEntity<BaseResponse> delete(@RequestParam(required = true) int id) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            service.delete(id);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

}
