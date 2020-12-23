package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import vn.com.insee.corporate.exception.InvalidSessionException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.security.InseeUserDetail;
import vn.com.insee.corporate.service.CustomerService;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;

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
    public ResponseEntity<BaseResponse> authenticate(@RequestBody Map<String, String> dataMap, Authentication authentication) throws InvalidSessionException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);

        String phone = dataMap.get("phone");
        phone = phone.replace("+", "");
        boolean isPhoneExits = service.isPhoneExits(phone);
        BaseResponse response = new BaseResponse(isPhoneExits ? ErrorCode.PHONE_EXITS : ErrorCode.SUCCESS);

        if (authUser != null) {
            Integer userId = authUser.getId();
            UserDTO userDTO = userService.findById(userId);
            if (userDTO == null) {
                throw new InvalidSessionException();
            }
            response.setData(userDTO);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse();
        try{
            UserEntity authUser = AuthenUtil.getAuthUser(authentication);


            CustomerDTO customerDTO = service.register(form, authUser != null ? authUser.getId() : null);
            if (customerDTO != null) {
                if (authUser != null) {
                    userService.update(authUser.getId(), customerDTO);
                }else {
                    //Create user and gen session
                }
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
