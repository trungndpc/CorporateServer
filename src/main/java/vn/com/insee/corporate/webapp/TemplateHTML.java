package vn.com.insee.corporate.webapp;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TemplateHTML {
    private static Map<String, String> CACHED = new HashMap<>();

    public static String load(String path) {
        String cache = CACHED.getOrDefault(path, null);
        if (cache == null) {
            try{
                File file = ResourceUtils.getFile("classpath:webapp/" + path + ".html");
                String content = new String(Files.readAllBytes(file.toPath()));
                cache = content;
                CACHED.put(path, content);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

}
