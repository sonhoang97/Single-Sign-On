package com.example.demooauth2.token;

import com.example.demooauth2.repository.RefreshTokenRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class JwtTokenService  extends  DefaultTokenServices {

    RefreshTokenRepository refreshTokenRepository;
    public JwtTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        //TODO check equal rftoken
        //if exist rftoken -> return new accesstoken
        // else thow new AuthenticationException

        return  super.refreshAccessToken(refreshTokenValue,tokenRequest);

    }
    @Override
    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        //TODO get rftoken by authentication
        //if exist -> super createAccessToken(OAuth2Authentication authentication, refresh token)
        // else super createAccessToken(authentication)
       return  super.createAccessToken(authentication);
    }



}
