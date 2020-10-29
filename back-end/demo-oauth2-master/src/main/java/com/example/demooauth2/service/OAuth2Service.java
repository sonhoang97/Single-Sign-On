package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;

public interface OAuth2Service {

    CommandResult revokeToken(String token, String refreshToken);
}
