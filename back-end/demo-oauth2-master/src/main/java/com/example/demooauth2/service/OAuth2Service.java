package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

public interface OAuth2Service {
    CommandResult responseAccessToken(Principal principal,Map<String, String> parameters);
    CommandResult revokeToken(String token, String refreshToken);
}
