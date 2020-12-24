package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.exception.InvalidSessionException;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.CustomerService;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.service.external.FirebaseService;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.ZaloUserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.util.HttpUtil;
import vn.com.insee.corporate.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static vn.com.insee.corporate.filter.CookieAuthenticationFilter.COOKIE_NAME;


@RestController
@RequestMapping("/authen")
public class AuthenController {

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Mapper mapper;


    @Autowired
    private FirebaseService firebaseService;

    @GetMapping(path = "/zalo")
    public RedirectView zalo(@RequestParam(required = false)
                                     String redirectUrl, HttpServletRequest request) throws UnsupportedEncodingException {
        String hookUrl = HttpUtil.getFullDomain(request) + "/authen/hook?c=" + URLEncoder.encode(redirectUrl, String.valueOf(StandardCharsets.UTF_8));
        System.out.println(hookUrl);
        String urlZaloAuthen = ZaloService.REDIRECT_AUTHEN_ZALO + "&redirect_uri=" + URLEncoder.encode(hookUrl, String.valueOf(StandardCharsets.UTF_8));
        return new RedirectView(urlZaloAuthen);
    }

    @GetMapping(path = "/hook")
    public RedirectView hook(@RequestParam(required = false) String c, @RequestParam(required = false) String code, HttpServletResponse resp) {
        try {
            String accessToken = zaloService.getAccessToken(code);
            ZaloUserEntity zaloUserEntity = zaloService.getUserInfo(accessToken);
            UserEntity userEntity = userService.initUserFromZalo(zaloUserEntity);
            if (userEntity != null) {
                genAndSetSession(userEntity.getId(), userEntity.getPhone(), resp);
                return new RedirectView(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/failed");
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody Map<String, String> dataMap, Authentication authentication, HttpServletResponse resp)  {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);

        try {
            String phone = dataMap.get("phone").replace("+", "");
//            String idToken = dataMap.get("idToken");
//
//            String firebaseUID = firebaseService.verifyTokenId(idToken);
//            String phoneToken = firebaseService.getUserPhoneNumberByUid(firebaseUID);
//            phoneToken = phoneToken.replace("+", "");
//            if (!phone.equals(phoneToken)) {
//                throw new FirebaseAuthenException(FirebaseAuthenException.FirebaseAuthenError.AUTH_ERROR);
//            }

            CustomerDTO customerDTO = customerService.findByPhone(phone);

            UserDTO userDTO = new UserDTO();
            if (authUser != null) {
                userDTO = userService.findById(authUser.getId());
                userService.updatePhone(authUser.getId(), phone);
                if (customerDTO != null) {
                    userService.linkCustomerIdToUser(authUser.getId(), customerDTO.getId());
                    customerService.linkCustomerToUserId(customerDTO.getId(), authUser.getId());
                }
            }else {
                RegisterForm registerForm = new RegisterForm();
                if (customerDTO != null) {
                    mapper.map(customerDTO, registerForm);
                }
                registerForm.setPhone(phone);
                userDTO = userService.create(registerForm);
                if (customerDTO != null && userDTO != null) {
                    userService.linkCustomerIdToUser(userDTO.getId(), customerDTO.getId());
                    customerService.linkCustomerToUserId(customerDTO.getId(), userDTO.getId());

                }

                genAndSetSession(userDTO.getId(), phone, resp);
            }
            response.setData(userDTO);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private void genAndSetSession(int id, String phone, HttpServletResponse resp) throws NotExitException {
        String session = TokenUtil.generate(id, phone, TokenUtil.MAX_AGE);
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(COOKIE_NAME, session)
                .path("/")
                .httpOnly(Boolean.TRUE)
                .secure(Boolean.TRUE)
                .sameSite("None")
                .maxAge(10 * 60 * 1000);
        String strCookie = cookieBuilder.build().toString();
        resp.addHeader("Set-Cookie", strCookie);
        userService.updateSession(id, session);
    }

}
