package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.modelView.tokens.Oauth2Token;
import com.example.demooauth2.repository.ClientDetailRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.TokenService;
import com.example.demooauth2.service.commons.AuthenticateService;
import com.example.demooauth2.service.commons.Oauth2Authentication;
import com.example.demooauth2.service.commons.TokenRequest;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("OAuth2Service")
public class OAuth2ServiceImpl implements OAuth2Service {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @Override
    public CommandResult responseAccessToken(Principal principal, Map<String, String> parameters) {
        try {
            if (!(principal instanceof Authentication)) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "There is no client authentication. Try adding an appropriate authentication filter.");
            }
            String clientId = this.getClientId(principal);
            ClientDetailViewModel authenticatedClient = clientDetailRepository.findByClientId(clientId);
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest = tokenRequest.createTokenRequest(parameters, authenticatedClient);

            if (clientId != null && !clientId.equals("") && !clientId.equals(tokenRequest.getClientDetail().getClientId())) {
                return new CommandResult(HttpStatus.CONFLICT, "Given client ID does not match authenticated client");
            }
            if (authenticatedClient != null) {
                this.validateScope(tokenRequest.getScopes(), authenticatedClient.getScope());
            }

            if (!StringUtils.hasText(tokenRequest.getGrantType())) {
                return new CommandResult(HttpStatus.NO_CONTENT, "Missing grant type");
            } else if (tokenRequest.getGrantType().equals("implicit")) {
                return new CommandResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Implicit grant type not supported from token endpoint");
            }
            Oauth2Token responseToken = this.grant(tokenRequest);

            return new CommandResult().SucceedWithData(responseToken);
        } catch (InvalidGrantException | UsernameNotFoundException | ExpiredJwtException ex) {
            return new CommandResult(HttpStatus.CONFLICT, ex.getMessage());
        } catch (NoSuchFieldException ex1) {
            return new CommandResult(HttpStatus.NOT_FOUND, ex1.getMessage());
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


    private Oauth2Token grant(TokenRequest tokenRequest) throws NoSuchFieldException {
        try {
            AuthenticateService authenticateService = new AuthenticateService(authenticationManager);
            Oauth2Authentication oauth2Authentication;
            if (this.isPasswordRequest(tokenRequest)) {
                oauth2Authentication = authenticateService.getOAuth2AuthenticationPassword(tokenRequest);
            } else if (this.isAuthCodeRequest(tokenRequest) && !tokenRequest.getScopes().isEmpty()) {
                oauth2Authentication = authenticateService.getOAuth2AuthenticationCode(tokenRequest);
            } else if (this.isRefreshTokenRequest(tokenRequest)) {
                return tokenService.createTokenByRefreshToken(tokenRequest);
            } else {
                throw new InvalidGrantException("Missing grant type");
            }
            return tokenService.createToken(oauth2Authentication);
        } catch (InvalidGrantException | UsernameNotFoundException ex) {
            throw new InvalidGrantException(ex.getMessage());
        }
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

    private boolean isRefreshTokenRequest(TokenRequest tokenRequest) {
        return "refresh_token".equals(tokenRequest.getGrantType()) && tokenRequest.getParameters().get("refresh_token") != null;
    }

    private boolean isPasswordRequest(TokenRequest tokenRequest) {
        return "password".equals(tokenRequest.getGrantType());
    }

    private boolean isAuthCodeRequest(TokenRequest tokenRequest) {
        return "authorization_code".equals(tokenRequest.getGrantType()) && tokenRequest.getParameters().get("code") != null;
    }

    private void validateScope(List<String> requestScopes, List<String> clientScopes) {
        if (clientScopes != null && !clientScopes.isEmpty()) {

            for (String scope : requestScopes) {
                if (!clientScopes.contains(scope)) {
                    throw new InvalidScopeException("Invalid scope: " + scope);
                }
            }
        }
        if (requestScopes.isEmpty()) {
            throw new InvalidScopeException("Empty scope (either the client or the user is not allowed the requested scopes)");
        }
    }


}
