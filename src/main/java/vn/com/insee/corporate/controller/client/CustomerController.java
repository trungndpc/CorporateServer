package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.CustomerService;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.io.UnsupportedEncodingException;

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



    @PostMapping(path = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> post(@RequestBody RegisterForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse();
        try{
            UserEntity authUser = AuthenUtil.getAuthUser(authentication);
            CustomerDTO customerDTO = service.createOrUpdate(form, authUser.getId());
            if (customerDTO != null) {
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

    @GetMapping(path = "/profile")
    public ResponseEntity<BaseResponse> getProfile(Authentication authentication) throws UnsupportedEncodingException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDTO userDTO = userService.findById(authUser.getId());
        Integer customerId = userDTO.getCustomerId();
        if (customerId == null) {
            response.setError(ErrorCode.USER_NOT_IS_CUSTOMER);
        }else{
            CustomerDTO customerDTO = service.get(customerId);
            response.setData(customerDTO);
        }
        return ResponseEntity.ok(response);
    }

}
