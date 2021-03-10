package vn.com.insee.corporate.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.StatusRegister;
import vn.com.insee.corporate.controller.api.client.AuthenController;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.ZaloUserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.util.HttpUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequestMapping("/prefix_client")
public class ClientController {
    private static final Logger LOGGER = LogManager.getLogger(ClientController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ZaloService zaloService;

    private static String PATH_INDEX_HTML_FILE = "client/index";
    private static String PATH_INDEX_HTML_OOPS = "client/oops";

    @GetMapping(value = "/dang-ky", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register(Authentication authentication, @RequestParam(required = false) String continueUrl,
                           @RequestParam(required = false) String code,
                           HttpServletResponse response) throws IOException, NotExitException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        LOGGER.info("/dang-ky?continueUrl=" + continueUrl + "&code=" + code);
        if (authUser == null && code == null) {
            String hook = Constant.CLIENT_DOMAIN + "/dang-ky";
            if (!StringUtils.isEmpty(continueUrl)) {
                hook = hook + "?continueUrl=" + continueUrl;
            }
            String urlAuthenZalo = Constant.ZALO_OA_REDIRECT_AUTHEN_ZALO + "&redirect_uri=" + HttpUtil.encodeUrl(hook);
            response.sendRedirect(urlAuthenZalo);
            LOGGER.info("redirect to authen zalo url: " + urlAuthenZalo);
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }

        if (authUser == null && code != null) {
            String accessToken = zaloService.getAccessToken(code);
            if (accessToken == null) {
                LOGGER.info("redirect to /Oops, accessToken is null");
                response.sendRedirect("/oops");
                return Constant.CONTENT_RESPONSE_TO_REDIRECT;
            }

            ZaloUserEntity zaloUserEntity = zaloService.getUserInfo(accessToken);
            if (zaloUserEntity == null) {
                LOGGER.info("redirect to /Oops, zaloUserEntity is null");
                response.sendRedirect("/oops");
                return Constant.CONTENT_RESPONSE_TO_REDIRECT;
            }

            authUser = userService.initUserFromZalo(zaloUserEntity);
            if (authUser == null) {
                LOGGER.info("redirect to /Oops, authUser is null");
                response.sendRedirect("/oops");
                return Constant.CONTENT_RESPONSE_TO_REDIRECT;
            }
            LOGGER.info("set session uid: " + authUser.getId() + ", phone: " + authUser.getPhone());
            String session =  AuthenController.genAndSetSession(authUser.getId(), authUser.getPhone(), response);
            userService.updateSession(authUser.getId(), session);
        }

        StatusRegister statusRegister = userService.getStatusRegister(authUser.getId());
        if (statusRegister == StatusRegister.REGISTERED || statusRegister == StatusRegister.WAITING_REVIEW) {
            String redirectURL = StringUtils.isEmpty(continueUrl) ? "/khach-hang" : continueUrl;
            LOGGER.info("redirect to " + redirectURL + ", status_register: " + statusRegister + ", uid: " + authUser.getId() + ", phone: " + authUser.getPhone());
            response.sendRedirect(redirectURL);
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }

        String html = TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
        JSONObject register = new JSONObject();
        if (statusRegister == StatusRegister.NOT_REGISTER) {
            LOGGER.info("render dang-ky step: 1");
            register.put("step", 1);
        } else {
            LOGGER.info("render dang-ky step: 3");
            register.put("step", 3);
        }
        html = TemplateHTML.initVariable(html, "register", register.toString());
        return html;
    }

    @GetMapping(value = "/oops", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String oops(Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws IOException {

        return TemplateHTML.client_load(PATH_INDEX_HTML_OOPS);
    }

    @GetMapping(value = "/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser == null || !isCustomer(authUser.getId())) {
            String continueUrl = HttpUtil.getFullDomain(request);
            continueUrl = HttpUtil.encodeUrl(continueUrl);
            LOGGER.info("redirect to /dang-ky?continueUrl=" +continueUrl);
            response.sendRedirect("/dang-ky?continueUrl=" + continueUrl);
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
    }

    private boolean isCustomer(int uid) {
        StatusRegister statusRegister = userService.getStatusRegister(uid);
        return statusRegister == StatusRegister.REGISTERED || statusRegister == StatusRegister.WAITING_REVIEW;
    }
}
