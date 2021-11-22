package com.example.shopme.backend.service.user.principal;

import com.example.shopme.backend.service.user.UserService;
import com.example.shopme.common.model.entity.user.User;
import com.example.shopme.common.model.entity.user.principal.UserPrincipalDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserPrincipalDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username != null && !username.isEmpty()) {
            Optional<User> byEmail = userService.findUserWithEmail(username);
            User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("Can Not found:" + username));
            return new UserPrincipalDetail(user);
        } else {
            throw new UsernameNotFoundException("user name could Not empty.");
        }
    }
}
