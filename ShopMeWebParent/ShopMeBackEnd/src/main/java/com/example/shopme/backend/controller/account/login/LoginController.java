package com.example.shopme.backend.controller.account.login;

import cn.apiclub.captcha.Captcha;
import com.example.shopme.backend.utils.captcha.CaptchaUtil;
import com.example.shopme.common.model.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class LoginController {


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        User user = new User();
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        return "account/login";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout() {
        final String LOGOUT = "?logout=logout.message";
        return "redirect:/login" + LOGOUT;
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    private void getCaptcha(User user) {
        Captcha captcha = CaptchaUtil.createCaptcha(170, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha(""); // value entered by the User
        user.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
    }
}
