package com.example.demooauth2.service.impl;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

@Service("OAuth2Service")
public class OAuth2ServiceImpl implements OAuth2Service {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public CommandResult responseAccessToken(Principal principal, Map<String, String> parameters) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException("There is no client authentication. Try adding an appropriate authentication filter.");
        } else {
            String clientId = this.getClientId(principal);
            ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
        }
        return new CommandResult().Succeed();
    }

    @Override
    public CommandResult revokeToken(String token, String refreshToken) {
        try {
            OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(token);
            OAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(refreshToken);
            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getClientId(Principal principal) {
        Authentication client = (Authentication)principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        } else {
            String clientId = client.getName();
            if (client instanceof OAuth2Authentication) {
                clientId = ((OAuth2Authentication)client).getOAuth2Request().getClientId();
            }

            return clientId;
        }
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }
}
