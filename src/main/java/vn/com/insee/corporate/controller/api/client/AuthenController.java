package vn.com.insee.corporate.controller.api.client;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.FirebaseAuthenException;
import vn.com.insee.corporate.exception.InvalidSessionException;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.exception.ZaloWebhookException;
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

    @Value("${authen.phone.need-check}")
    private boolean isNeedCheckPhone;

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping(path = "/zalo")
    public RedirectView zalo(@RequestParam(required = false)
                                     String redirectUrl, HttpServletRequest request) throws UnsupportedEncodingException {
        String hookUrl = HttpUtil.getFullDomain(request) + "/authen/hook?c=" + URLEncoder.encode(redirectUrl, String.valueOf(StandardCharsets.UTF_8));
        String urlZaloAuthen = Constant.ZALO_OA_REDIRECT_AUTHEN_ZALO + "&redirect_uri=" + URLEncoder.encode(hookUrl, String.valueOf(StandardCharsets.UTF_8));
        return new RedirectView(urlZaloAuthen);
    }

    @GetMapping(path = "/hook")
    public RedirectView hook(@RequestParam(required = false) String c, @RequestParam(required = false) String code, HttpServletResponse resp) {
        try {
            String accessToken = zaloService.getAccessToken(code);
            ZaloUserEntity zaloUserEntity = zaloService.getUserInfo(accessToken);
            UserEntity userEntity = userService.initUserFromZalo(zaloUserEntity);
            if (userEntity != null) {
                String session = genAndSetSession(userEntity.getId(), userEntity.getPhone(), resp);
                userService.updateSession(userEntity.getId(), session);
                return new RedirectView(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/failed");
    }

    @PostMapping(value = "/zalo-webhook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> zaloWebhook(@RequestBody String body) throws InvalidSessionException {
        System.out.println("ZALO--------WEBHOOK-----------------------------");
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            JSONObject jsonBody = new JSONObject(body);
            String eventName = jsonBody.optString("event_name", null);
            String followerId = getFollowerId(jsonBody);
            if (followerId == null) {
                throw new ZaloWebhookException();
            }
            if ("follow".equals(eventName)) {
                String zaloId = jsonBody.optString("user_id_by_app", null);
                if (zaloId != null) {
                    userService.linkFollowerZaloWithUser(zaloId, followerId);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private String getFollowerId(JSONObject jsonBody) {
        try{
            JSONObject follower = jsonBody.getJSONObject("follower");
            return follower.getString("id");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "/check-phone", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody Map<String, String> dataMap, Authentication authentication) throws InvalidSessionException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);

        String phone = dataMap.get("phone");
        phone = phone.replace("+", "");
        UserDTO userDTO = userService.findByPhone(phone);
        BaseResponse response = new BaseResponse(userDTO != null ? ErrorCode.PHONE_EXITS : ErrorCode.SUCCESS);

        if (authUser != null && userDTO == null) {
            CustomerDTO customerDTO = customerService.findByPhone(phone);
            if (customerDTO != null) {
                Integer userId = authUser.getId();
                Integer customerUserID = customerDTO.getUserId();
                if (customerUserID == null || userId == customerUserID) {
                    response.setData(customerDTO);
                }
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody Map<String, String> dataMap, Authentication authentication, HttpServletResponse resp)  {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);

        try {
            String phone = dataMap.get("phone").replace("+", "");
            if (isNeedCheckPhone) {
                String idToken = dataMap.get("idToken");
                String firebaseUID = firebaseService.verifyTokenId(idToken);
                String phoneToken = firebaseService.getUserPhoneNumberByUid(firebaseUID);
                phoneToken = phoneToken.replace("+", "");
                if (!phone.equals(phoneToken)) {
                    throw new FirebaseAuthenException(FirebaseAuthenException.FirebaseAuthenError.AUTH_ERROR);
                }
            }

            CustomerDTO customerDTO = customerService.findByPhone(phone);
            if (authUser != null) {
                UserDTO userDTO = userService.updatePhone(authUser.getId(), phone);
                if (customerDTO != null) {
                    userService.linkCustomerIdToUser(authUser.getId(), customerDTO.getId());
                    customerService.linkCustomerToUserId(customerDTO.getId(), authUser.getId());
                }
                response.setData(userDTO);
            }else {
                RegisterForm registerForm = new RegisterForm();
                if (customerDTO != null) {
                    mapper.map(customerDTO, registerForm);
                }
                registerForm.setPhone(phone);
                UserDTO userDTO = userService.create(registerForm, Permission.CUSTOMER);
                if (customerDTO != null && userDTO != null) {
                    userService.linkCustomerIdToUser(userDTO.getId(), customerDTO.getId());
                    customerService.linkCustomerToUserId(customerDTO.getId(), userDTO.getId());
                }
                String session = genAndSetSession(userDTO.getId(), phone, resp);
                userService.updateSession(userDTO.getId(), session);
                response.setData(userDTO);
            }
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    public static String genAndSetSession(int id, String phone, HttpServletResponse resp) throws NotExitException {
        String session = TokenUtil.generate(id, phone, TokenUtil.MAX_AGE);
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(COOKIE_NAME, session)
                .path("/")
                .httpOnly(Boolean.TRUE)
                .secure(Boolean.TRUE)
                .sameSite("None")
                .maxAge(10 * 60 * 1000);
        String strCookie = cookieBuilder.build().toString();
        resp.addHeader("Set-Cookie", strCookie);
        return session;
    }

}
