package vn.com.insee.corporate.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/prefix_admin")
public class AdminController {

    private static String PATH_INDEX_HTML_FILE = "admin/index";

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String login(Authentication authentication, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null) {
            response.sendRedirect("/");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.admin_load(PATH_INDEX_HTML_FILE);
    }

    @GetMapping(value = "/**", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(Authentication authentication, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser == null) {
            response.sendRedirect("/login");
            return Constant.CONTENT_RESPONSE_TO_REDIRECT;
        }
        return TemplateHTML.admin_load(PATH_INDEX_HTML_FILE);
    }

}
