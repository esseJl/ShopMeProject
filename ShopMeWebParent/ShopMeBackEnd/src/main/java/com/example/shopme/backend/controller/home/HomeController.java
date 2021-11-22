package com.example.shopme.backend.controller.home;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController implements ErrorController {

    @RequestMapping("/home")
    public String home() {
        return "index";
    }
    

    @RequestMapping("/error")
    public String errorPage() {
        return "error/default";
    }
}
