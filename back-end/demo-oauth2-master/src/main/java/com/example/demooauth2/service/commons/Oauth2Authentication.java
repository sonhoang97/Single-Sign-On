package com.example.demooauth2.service.commons;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
@Getter
@Setter
public class Oauth2Authentication {
    private final TokenRequest tokenRequest;
    private final Authentication authentication;
    public Oauth2Authentication(TokenRequest tokenRequest, Authentication authentication) {
        this.tokenRequest = tokenRequest;
        this.authentication = authentication;
    }
}
