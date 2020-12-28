package vn.com.insee.corporate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class StaticController {

    @GetMapping(path = "/images/{fileName}")
    @ResponseBody
    public RedirectView index(@PathVariable String fileName) {
        return new RedirectView("https://trungndpc.github.io/insee/insee-promotion-client/dist/images/" + fileName);
    }
}
