package com.example.demooauth2.service.commons;

import com.example.demooauth2.repository.OauthCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticateService {
    private final AuthenticationManager authenticationManager;
    @Autowired
    private OauthCodeRepository oauthCodeRepository;
    public AuthenticateService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Oauth2Authentication getOAuth2AuthenticationPassword(TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getParameters());
        String username = (String)parameters.get("username");
        String password = (String)parameters.get("password");
        parameters.remove("password");
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException var8) {
            throw new InvalidGrantException(var8.getMessage());
        }
        if (userAuth != null && userAuth.isAuthenticated()) {
            return new Oauth2Authentication(tokenRequest,userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
    }

    public Oauth2Authentication getOAuth2AuthenticationCode(TokenRequest tokenRequest){
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getParameters());
        String code = (String)parameters.get("code");
        Authentication userAuth = oauthCodeRepository.getAuthentication(code);
        if(userAuth==null || !userAuth.isAuthenticated()){
            throw new InvalidGrantException("Could not authenticate user");
        }
        oauthCodeRepository.deleteCode(code);
        parameters.remove("code");
        tokenRequest.setParameters(parameters);
        return new Oauth2Authentication(tokenRequest,userAuth);
    }
}
