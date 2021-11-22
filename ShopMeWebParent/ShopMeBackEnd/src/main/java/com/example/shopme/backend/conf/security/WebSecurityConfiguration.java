package com.example.shopme.backend.conf.security;

import com.example.shopme.backend.filter.auth.captcha.CustomAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.nio.file.attribute.UserPrincipal;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder encoder;
    private final Environment environment;

    @Autowired
    public WebSecurityConfiguration(UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler, PasswordEncoder encoder, Environment environment) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.encoder = encoder;
        this.environment = environment;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/**/admin/**").hasRole("ADMIN")
                .antMatchers("/**/user/**").hasAnyRole("USER", "ADMIN")

                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
                .and().logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .and().httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return ((request, response, authentication) -> {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            //System.out.println(principal.getUsername());

            //TODO filter principal for redirect
            response.sendRedirect(getServerContextPath());
        });
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return ((request, response, exception) -> {
            String message = exception.getMessage();
            response.sendRedirect(getServerContextPath() + "login?error=" + message);
        });
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect(getServerContextPath() + "login?logout");
        });
    }

    private String getServerContextPath() {

        if (!environment.containsProperty("server.servlet.context-path")) {
            return "/";
        } else {
            String serverContextPath = environment.getProperty("server.servlet.context-path");
            if (serverContextPath == null || serverContextPath.trim().equals(""))
                return "/";
            else
                return serverContextPath + "/";
        }
    }


}
