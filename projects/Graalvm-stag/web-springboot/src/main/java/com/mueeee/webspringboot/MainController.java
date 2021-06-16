package com.mueeee.webspringboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Controller
 */
@Controller
public class MainController {



    @ResponseBody
    @GetMapping("/helloworld")
    public Object foo() {
        return new HashMap<String, Object>() {{
            put("code", 0);
            put("message", "helloworld");
        }};
    }

}
