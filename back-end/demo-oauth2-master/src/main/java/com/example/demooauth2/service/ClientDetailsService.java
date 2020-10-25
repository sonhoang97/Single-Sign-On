package com.example.demooauth2.service;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.User;
import org.springframework.security.oauth2.provider.ClientDetails;

public interface ClientDetailsService {
    CommandResult createClientDetail(String clientId, String redirectUri);

    ClientDetails loadClientByClientId(String clientId);

    CommandResult updateClientSecret(String clientId);

    CommandResult updateRedirectUri(String clientId, String redirectUri);

    CommandResult removeClientDetail(String clientId);
}
