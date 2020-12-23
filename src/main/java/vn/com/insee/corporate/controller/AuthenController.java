package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.ZaloUserEntity;
import vn.com.insee.corporate.util.HttpUtil;
import vn.com.insee.corporate.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static vn.com.insee.corporate.filter.CookieAuthenticationFilter.ZALO_COOKIE_NAME;

@RestController
@RequestMapping("/authen")
public class AuthenController {

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/zalo")
    public RedirectView zalo(@RequestParam(required = false)
                                     String redirectUrl, HttpServletRequest request) throws UnsupportedEncodingException {
        String hookUrl = HttpUtil.getFullDomain(request) + "/authen/hook?c=" + URLEncoder.encode(redirectUrl, String.valueOf(StandardCharsets.UTF_8));
        String urlZaloAuthen = ZaloService.REDIRECT_AUTHEN_ZALO + "&redirect_uri=" + URLEncoder.encode(hookUrl, String.valueOf(StandardCharsets.UTF_8));
        return new RedirectView(urlZaloAuthen);
    }

    @GetMapping(path = "/hook")
    public RedirectView hook(@RequestParam(required = false) String c, @RequestParam(required = false) String code, HttpServletRequest req) {
        try {
            String accessToken = zaloService.getAccessToken(code);
            ZaloUserEntity zaloUserEntity = zaloService.getUserInfo(accessToken);
            UserEntity userEntity = userService.initUserFromZalo(zaloUserEntity);
            if (userEntity != null) {
                genAndSetSession(userEntity.getId(), userEntity.getPhone(), req);
                return new RedirectView(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RedirectView("/failed");
    }

    private void genAndSetSession(int id, String phone, HttpServletRequest req) throws NotExitException {
        String session = TokenUtil.generate(id, phone, TokenUtil.MAX_AGE);
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(ZALO_COOKIE_NAME, session)
                .path("/")
                .httpOnly(Boolean.TRUE)
                .secure(Boolean.TRUE)
                .sameSite("None")
                .maxAge(10 * 60 * 1000);
        String strCookie = cookieBuilder.build().toString();
        req.setAttribute(ZALO_COOKIE_NAME, strCookie);
        userService.updateSession(id, session);
    }
}
