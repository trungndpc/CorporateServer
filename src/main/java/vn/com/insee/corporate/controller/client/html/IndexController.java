package vn.com.insee.corporate.controller.client.html;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.com.insee.corporate.common.UserStatusEnum;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class IndexController {

    @GetMapping(value = "/*", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index() {
        String html = TemplateHTML.load("client/index");
        return html;
    }


    @GetMapping(value = "/dang-ky", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser != null) {
            Integer customerId = authUser.getCustomerId();
            if (customerId != null) {
                response.sendRedirect("/khach-hang");
                return "OK";
            }
        }else {
            System.out.println(userAgent);
            if (userAgent.contains("Zalo")) {
                response.sendRedirect("/authen/zalo?redirectUrl=" + "https%3A%2F%2Finsee-promotion.herokuapp.com%2Fdang-ky");
                return "OK";
            }
        }
        return index();
    }


    @GetMapping(value = "/khach-hang", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String login(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        if (authUser == null) {
            response.sendRedirect("/dang-nhap");
            return "OK";
        }
        return index();
    }





}
