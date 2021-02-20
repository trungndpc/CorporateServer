package vn.com.insee.corporate.controller.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.util.TokenUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static vn.com.insee.corporate.filter.CookieAuthenticationFilter.COOKIE_NAME;

@RestController
@RequestMapping("/api/admin/authen")
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
            String session = genAndSetSession(userDTO.getId(), userDTO.getPhone(), resp);
            userService.updateSession(userDTO.getId(), session);
            response.setError(ErrorCode.SUCCESS);
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
        response.setData(userDTO);
        return ResponseEntity.ok(response);
    }

    public static String genAndSetSession(int id, String phone, HttpServletResponse resp) throws NotExitException {
        String session = TokenUtil.generate(id, phone, TokenUtil.MAX_AGE);
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(COOKIE_NAME, session)
                .path("/")
                .httpOnly(Boolean.TRUE)
                .secure(Boolean.FALSE)
                .sameSite("Lax")
                .maxAge(10 * 60 * 1000);
        String strCookie = cookieBuilder.build().toString();
        resp.addHeader("Set-Cookie", strCookie);
        return session;
    }

}
