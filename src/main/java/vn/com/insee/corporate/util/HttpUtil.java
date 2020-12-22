package vn.com.insee.corporate.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

    public static String getCookie(String name, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }
        for (Cookie co : cookies) {
            if (name.equals(co.getName())) {
                return co.getValue();
            }
        }
        return null;
    }
}
