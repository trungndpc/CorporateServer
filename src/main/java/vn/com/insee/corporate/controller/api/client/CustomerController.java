package vn.com.insee.corporate.controller.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
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

    @Autowired
    private CustomerService customerService;


    @PostMapping(path = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> post(@RequestBody RegisterForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse();
        try{
            UserEntity authUser = AuthenUtil.getAuthUser(authentication);
            CustomerDTO customerDTO = service.createOrUpdate(form, authUser.getId());
            if (customerDTO != null) {
                userService.linkCustomerIdToUser(authUser.getId(), customerDTO.getId());
                userService.updatePassword(authUser.getId(), form.getPass());

                //AUTO APPROVED
                customerService.updateStatus(customerDTO.getId(), CustomerStatus.APPROVED, null);
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
        UserDTO userDTO = userService.get(authUser.getId());
        Integer customerId = userDTO.getCustomerId();
        if (customerId == null) {
            response.setError(ErrorCode.USER_NOT_IS_CUSTOMER);
        }else{
            CustomerDTO customerDTO = service.get(customerId);
            if (customerDTO.getAvatar() == null) {
                customerDTO.setAvatar(userDTO.getAvatar());
            }
            response.setData(customerDTO);
        }
        return ResponseEntity.ok(response);
    }

}
