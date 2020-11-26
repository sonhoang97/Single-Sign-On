package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.security.Principal;
import java.util.List;

public interface ClientDetailsService {
    CommandResult createClientDetail(Principal principal,String clientId, List<String> redirectUri);

    ClientDetails loadClientByClientId(String clientId);

    CommandResult updateClientSecret(String clientId);

    CommandResult updateRedirectUri(String clientId, String redirectUri);

    CommandResult removeClientDetail(String clientId);

    CommandResult getClientsByUserId(Principal principal);
}
