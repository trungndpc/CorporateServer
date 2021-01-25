package vn.com.insee.corporate.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/prefix_client")
public class ClientController {

    private static String PATH_INDEX_HTML_FILE = "client/index";

    @GetMapping(value = "/dang-ky", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                           HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null) {
            if (authUser.getRoleId() == Permission.ANONYMOUS.getId()) {
                return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
            }else {
                response.sendRedirect("/khach-hang");
                return Constant.CONTENT_RESPONSE_TO_REDIRECT;
            }
        }else {
            if (userAgent.contains("Zalo")) {
                response.sendRedirect("/authen/zalo?redirectUrl=" + "https%3A%2F%2Finsee-client.wash-up.vn%2Fdang-ky");
            } else {
                response.sendRedirect("/dang-nhap");
            }
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
    }

    @GetMapping(value = "/dang-nhap", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String login(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null) {
            response.sendRedirect("/khach-hang");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
    }

    @GetMapping(value = "/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(Authentication authentication, HttpServletResponse response) throws IOException {
        System.out.println("index**");
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser == null) {
            response.sendRedirect("/dang-nhap");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.client_load(PATH_INDEX_HTML_FILE);
    }


}
