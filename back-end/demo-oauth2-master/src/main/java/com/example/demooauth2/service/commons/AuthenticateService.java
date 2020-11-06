package com.example.demooauth2.service.commons;

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

    public AuthenticateService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public OAuth2Authentication getOAuth2Authentication( TokenRequest tokenRequest) {
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
        return null;
//        if (userAuth != null && userAuth.isAuthenticated()) {
//            //todo: Generate JWT
////            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
//            return new OAuth2Authentication();
//        } else {
//            throw new InvalidGrantException("Could not authenticate user: " + username);
//        }
    }
}
