package com.example.shopme.backend.filter.auth.captcha;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private String processUrl;
    //private String failureUrl;

    public CustomAuthenticationFilter() {
        super();
        this.processUrl = "/login";
        //this.failureUrl = "/login?captchaError=not.match.captcha";
        //setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (processUrl.equals(req.getServletPath()) && "POST".equalsIgnoreCase(req.getMethod())) {
            String hiddenCaptcha = req.getParameter("hiddenCaptcha");
            //System.out.println(hiddenCaptcha);
            if (hiddenCaptcha == null || hiddenCaptcha.trim().equals("")) {
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Wrong verification code."));
                return;
            }
            String expect = req.getSession().getAttribute(hiddenCaptcha).toString();
            if (expect == null || expect.trim().equals("")) {

                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Wrong verification code."));
                return;
            }
            req.getSession().removeAttribute(hiddenCaptcha);
            String captcha = req.getParameter("captcha");
            if (captcha == null || captcha.equals("")) {
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Wrong verification code. cant empty"));
                return;
            }
            if (!captcha.trim().equals(expect)) {
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Wrong verification code. cant Match captcha"));
                //req.setAttribute("captchaError", "not.match.captcha");
                return;
            }
            //System.out.println(expect);

        }
        chain.doFilter(request, response);
        //super.doFilter(request, response, chain);
    }
}

/*

*
 String expect = req.getSession().getAttribute("hiddenCaptcha").toString();
            //remove from session
            req.getSession().removeAttribute("YZM");

            if (expect != null && !expect.equals(req.getParameter("verifyCode"))) {
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Wrong verification code."));
                return;
            }
*/