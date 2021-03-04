package vn.com.insee.corporate.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.StatusRegister;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.util.HttpUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/prefix_client")
public class ClientController {

    @Autowired
    private UserService userService;
    private static String PATH_INDEX_HTML_FILE = "client/index";

    @GetMapping(value = "/dang-ky", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                           HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null) {
            String html = TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
            StatusRegister statusRegister = userService.getStatusRegister(authUser.getId());
            if (statusRegister == StatusRegister.NOT_REGISTER) {
                JSONObject register = new JSONObject();
                register.put("step", 1);
                html = TemplateHTML.initVariable(html, "register", register.toString());
            }else if (statusRegister == StatusRegister.NOT_CUSTOMER) {
                JSONObject register = new JSONObject();
                register.put("step", 3);
                html = TemplateHTML.initVariable(html, "register", register.toString());
            }else if (statusRegister == StatusRegister.WAITING_REVIEW || statusRegister == StatusRegister.REGISTERED) {
                    response.sendRedirect("/khach-hang");
                    return Constant.CONTENT_RESPONSE_TO_REDIRECT;
            }else {
                JSONObject register = new JSONObject();
                register.put("step", 3);
                html = TemplateHTML.initVariable(html, "register", register.toString());
            }
            return html;
        }else {
            String redirectUrl = HttpUtil.encodeUrl(Constant.CLIENT_DOMAIN + "/dang-ky");
            response.sendRedirect("/authen/zalo?redirectUrl=" + redirectUrl);
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
    }

    @GetMapping(value = "/dang-nhap", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String login(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null ) {
            response.sendRedirect("/khach-hang");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
    }

    @GetMapping(value = "/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(Authentication authentication, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser == null) {
            response.sendRedirect("/dang-ky");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }else if (authUser.getRoleId()  == null || authUser.getRoleId() == Permission.ANONYMOUS.getId()){
            response.sendRedirect("/dang-ky");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
    }
}
