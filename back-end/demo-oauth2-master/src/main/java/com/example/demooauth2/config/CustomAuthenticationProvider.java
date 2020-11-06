package com.example.demooauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authenticate) throws AuthenticationException {
        //
        try {
            UserDetails user = userDetailService.loadUserByUsername(authenticate.getName());
            String username = authenticate.getName();
            String password = authenticate.getCredentials().toString();
            UsernamePasswordAuthenticationToken result = null;
            if (user.getUsername().equals(username) && passwordEncoder.matches(password,user.getPassword())) {
                result = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<GrantedAuthority>());
            }
            return result;
        } catch (UsernameNotFoundException e) {
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public void setUserDetailService(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }
}
