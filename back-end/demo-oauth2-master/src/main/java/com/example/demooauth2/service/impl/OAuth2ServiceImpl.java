package com.example.demooauth2.service.impl;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.commons.AuthenticateService;
import com.example.demooauth2.service.commons.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service("OAuth2Service")
public class OAuth2ServiceImpl implements OAuth2Service {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private AuthenticateService authenticateService;

    @Override
    public CommandResult responseAccessToken(Principal principal, Map<String, String> parameters) {
        try {
            if (!(principal instanceof Authentication)) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "There is no client authentication. Try adding an appropriate authentication filter.");
            } else {
                String clientId = this.getClientId(principal);
                ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
                TokenRequest tokenRequest = new TokenRequest();
                tokenRequest = tokenRequest.createTokenRequest(parameters, authenticatedClient);

                if (clientId != null && !clientId.equals("") && !clientId.equals(tokenRequest.getClientId())) {
                    return new CommandResult(HttpStatus.CONFLICT, "Given client ID does not match authenticated client");
                } else {
                    if (authenticatedClient != null) {
                        this.validateScope((Set<String>) tokenRequest.getScopes(), authenticatedClient.getScope());
                    }

                    if (!StringUtils.hasText(tokenRequest.getGrantType())) {
                        return new CommandResult(HttpStatus.NO_CONTENT, "Missing grant type");
                    } else if (tokenRequest.getGrantType().equals("implicit")) {
                        return new CommandResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Implicit grant type not supported from token endpoint");
                    } else {
                        if (this.isAuthCodeRequest(parameters) && !tokenRequest.getScopes().isEmpty()) {
                            //Todo: Something with oauth code
//                            tokenRequest.setScopes(Collections.emptySet());
                        }

                        if (this.isRefreshTokenRequest(parameters)) {
                            //Todo: Something with refresh token
//                            tokenRequest.setScopes(OAuth2Utils.parseParameterList((String)parameters.get("scope")));
                        }

                        //Todo: Return JWT
                        this.grant(tokenRequest);
                    }
                }
            }
            return new CommandResult().Succeed();
        } catch (InvalidScopeException ex) {
            return new CommandResult(HttpStatus.CONFLICT, "Invalid scope");
        }
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

    private String grant(TokenRequest tokenRequest) {
        if (this.isPasswordRequest(tokenRequest.getParameters())) {
            //todo: Authenticate username and password
            authenticateService = new AuthenticateService(authenticationManager);
            OAuth2Authentication test = authenticateService.getOAuth2Authentication(tokenRequest);
        }
        return null;
    }

    private String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        } else {
            String clientId = client.getName();
            if (client instanceof OAuth2Authentication) {
                clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
            }

            return clientId;
        }
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isPasswordRequest(Map<String, String> parameters) {
        return "password".equals(parameters.get("grant_type"));
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    private void validateScope(Set<String> requestScopes, Set<String> clientScopes) {
        if (clientScopes != null && !clientScopes.isEmpty()) {

            for (String scope : requestScopes) {
                if (!clientScopes.contains(scope)) {
                    throw new InvalidScopeException("Invalid scope: " + scope, clientScopes);
                }
            }
        }
        if (requestScopes.isEmpty()) {
            throw new InvalidScopeException("Empty scope (either the client or the user is not allowed the requested scopes)");
        }
    }


}
