package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import java.security.Principal;
import java.util.Map;

public interface OAuth2Service {
    CommandResult revokeToken(String token, String refreshToken);
    CommandResult responseAuthorizationCode(Map<String, String> parameters, Principal principal);
}
