package com.launch.nazmul.launch.services.impl;

import com.launch.nazmul.launch.commons.utils.NetworkUtil;
import com.launch.nazmul.launch.entities.User;
import com.launch.nazmul.launch.exceptions.notfound.UserNotFoundException;
import com.launch.nazmul.launch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public CustomUserDetailsService(UserService userService, LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // flood control
        String ip = NetworkUtil.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
        // end flood control
        User user;
        try {
            user = this.userService.findByUsernameOrPhone(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User doesn't exist!");
        }
        if (user == null) throw new UsernameNotFoundException("User doesn't exist!");
        return user;
    }

}
