package vn.com.insee.corporate.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public static String getFullDomain(HttpServletRequest request) {
        try{
            URL url = new URL(request.getRequestURL().toString());
            String host  = url.getHost();
            String scheme = url.getProtocol();
            int port = url.getPort();
            URI uri = new URI("https",null,host,port,null,null,null);
            return uri.toString().replace("https://localhost:8080", "https://b32835fb13d9.ngrok.io");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
