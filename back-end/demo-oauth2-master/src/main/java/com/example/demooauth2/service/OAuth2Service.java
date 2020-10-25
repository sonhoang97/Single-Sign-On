package com.example.demooauth2.service;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.AccessToken;

public interface OAuth2Service {

    CommandResult revokeToken(String token, String refreshToken);
}
