package vn.com.insee.corporate.webapp;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import vn.com.insee.corporate.common.Constant;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class TemplateHTML {
    private static Map<String, String> CACHED = new HashMap<>();

    public static String admin_load(String path) {
        String cache = CACHED.getOrDefault(path, null);
        if (cache == null) {
            try{
                InputStream inputStream = new ClassPathResource("webapp/" + path + ".html").getInputStream();
                cache = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
                cache = cache.replaceAll("\\{\\{domain\\}\\}", Constant.ADMIN_DOMAIN);
                cache = cache.replaceAll("\\{\\{version\\}\\}", Constant.ADMIN_DOMAIN_VERSION);
                CACHED.put(path, cache);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

    public static String client_load(String path) {
        String cache = CACHED.getOrDefault(path, null);
        if (cache == null) {
            try{
                InputStream inputStream = new ClassPathResource("webapp/" + path + ".html").getInputStream();
                cache = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
                cache = cache.replaceAll("\\{\\{domain\\}\\}", Constant.CLIENT_DOMAIN);
                cache = cache.replaceAll("\\{\\{version\\}\\}", Constant.CLIENT_DOMAIN_VERSION);
                CACHED.put(path, cache);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }



}
