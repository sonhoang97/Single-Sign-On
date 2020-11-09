package com.example.demooauth2.service;

import com.example.demooauth2.modelView.tokens.Oauth2Token;
import com.example.demooauth2.service.commons.Oauth2Authentication;
import com.example.demooauth2.service.commons.TokenRequest;

import java.io.IOException;

public interface TokenService {
    Oauth2Token createToken(Oauth2Authentication oauth2Authentication) throws NoSuchFieldException;

    Oauth2Token createTokenByRefreshToken(TokenRequest tokenRequest);
}
