package vn.com.insee.corporate.controller.client.html;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.insee.corporate.webapp.TemplateHTML;


@Controller
public class IndexController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index() {
        String html = TemplateHTML.load("client/index");
        System.out.println(html);
        return html;
    }

}
