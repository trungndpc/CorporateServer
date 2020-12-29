package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.controller.AuthenController;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/admin/authen")
public class AuthenAdminController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> loginWithPassword(@RequestBody Map<String, String> dataMap, Authentication authentication, HttpServletResponse resp)  {
        BaseResponse response = new BaseResponse(ErrorCode.FAILED);
        try {
            String phone = dataMap.get("phone").replace("+", "");
            String pass = dataMap.getOrDefault("pass", null);
            UserDTO userDTO = userService.loginWithPassword(phone, pass, Permission.ADMIN);
            String session = AuthenController.genAndSetSession(userDTO.getId(), userDTO.getPhone(), resp);
            userService.updateSession(userDTO.getId(), session);
            response.setError(ErrorCode.SUCCESS);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

}
