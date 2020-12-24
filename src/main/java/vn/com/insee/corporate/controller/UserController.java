package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/profile")
    public ResponseEntity<BaseResponse> getUserInfo(Authentication authentication) throws UnsupportedEncodingException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDTO userDTO = userService.findById(authUser.getId());
        response.setData(userDTO);
        return ResponseEntity.ok(response);
    }
}
