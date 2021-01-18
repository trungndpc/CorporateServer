package vn.com.insee.corporate.webapp;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static vn.com.insee.corporate.controller.IndexController.DOMAIN_ADMIN;
import static vn.com.insee.corporate.controller.IndexController.DOMAIN_CLIENT;

public class TemplateHTML {
    private static Map<String, String> CACHED = new HashMap<>();

    public static String load(String path, boolean isAdmin) {
        String cache = CACHED.getOrDefault(path + (isAdmin ? "admin" : "client"), null);
        if (cache == null) {
            try{
                InputStream inputStream = new ClassPathResource("webapp/" + path + ".html").getInputStream();
                String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
                if (isAdmin) {
                    content  = content.replace("{{domain}}", DOMAIN_ADMIN);
                    content = content.replace("{{editor_script}}", "<script src=\"https://insee-promotion-vn.s3.us-east-2.amazonaws.com/static/ckeditor.js\"></script>\n");
                }else {
                    content  = content.replace("{{domain}}", DOMAIN_CLIENT);
                    content = content.replace("{{editor_script}}", "");
                }
                cache = content;
                CACHED.put(path, content);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

}
