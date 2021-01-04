package vn.com.insee.corporate.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.util.AuthenUtil;
import vn.com.insee.corporate.webapp.TemplateHTML;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class IndexController {

    private static final String DOMAIN_CLIENT = "insee-client.wash-up.vn";
    private static final String DOMAIN_ADMIN = "insee-admin-client.wash-up.vn";
    private static final String TEXT_RESPONSE_WHEN_REDIRECT = "OK";
    private static final String TEXT_RESPONSE_WHEN_EXCEPTION = "Please try again!";
    private static final String TEXT_RESPONSE_WHEN_NOT_FOUND_PATH = "Not found!";

    @GetMapping(value = "/*", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(HttpServletRequest request, Authentication authentication, HttpServletResponse response) {
        try {
            UserEntity authUser = AuthenUtil.getAuthUser(authentication);
            String domain = getDomain(request);
            System.out.println("domain: " + domain);
            if (DOMAIN_ADMIN.equals(domain)) {
                if (authUser == null || authUser.getRoleId() != Permission.ADMIN.getId()) {
                    response.sendRedirect("/login");
                    return TEXT_RESPONSE_WHEN_REDIRECT;
                }
            }
            if (DOMAIN_CLIENT.equals(domain)) {
                if (authUser == null) {
                    response.sendRedirect("/khuyen-mai");
                    return TEXT_RESPONSE_WHEN_REDIRECT;
                }
            }
            return TemplateHTML.load("client/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TEXT_RESPONSE_WHEN_EXCEPTION;
    }


    @GetMapping(value = "/dang-ky", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_ADMIN.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        if (authUser != null && authUser.getRoleId() != Permission.ANONYMOUS.getId()) {
            response.sendRedirect("/khach-hang");
        } else if (authUser != null && authUser.getRoleId() == Permission.ANONYMOUS.getId()) {
            return TemplateHTML.load("client/index");
        } else {
            if (userAgent.contains("Zalo")) {
                response.sendRedirect("/authen/zalo?redirectUrl=" + "https%3A%2F%2Finsee-client.wash-up.vn%2Fdang-ky");
            } else {
                response.sendRedirect("/dang-nhap");
            }
            return TEXT_RESPONSE_WHEN_REDIRECT;
        }
        return TemplateHTML.load("client/index");
    }


    @GetMapping(value = "/khach-hang", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String adminLogin(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_ADMIN.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        if (authUser == null) {
            response.sendRedirect("/dang-nhap");
            return TEXT_RESPONSE_WHEN_REDIRECT;
        }
        return TemplateHTML.load("client/index");
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String profile(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_CLIENT.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        if (authUser != null) {
            response.sendRedirect("/");
            return TEXT_RESPONSE_WHEN_REDIRECT;
        }
        return TemplateHTML.load("client/index");
    }

    @GetMapping(value = "/khuyen-mai", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String clientPromotion(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_ADMIN.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        return TemplateHTML.load("client/index");
    }

    @GetMapping(value = "/dang-nhap", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String clientLogin(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_ADMIN.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        if (authUser != null) {
            response.sendRedirect("/khach-hang");
            return TEXT_RESPONSE_WHEN_REDIRECT;
        }
        return TemplateHTML.load("client/index");
    }

    @GetMapping(value = "/customer/*", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String detailCustomer(Authentication authentication, @RequestHeader(value = "User-Agent") String userAgent,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        String domain = getDomain(request);
        if (DOMAIN_CLIENT.equals(domain)) {
            return TEXT_RESPONSE_WHEN_NOT_FOUND_PATH;
        }
        if (authUser == null) {
            response.sendRedirect("/login");
            return TEXT_RESPONSE_WHEN_REDIRECT;
        }
        return TemplateHTML.load("client/index");
    }


    public String getDomain(HttpServletRequest request) {
        return request.getServerName();
    }


}
