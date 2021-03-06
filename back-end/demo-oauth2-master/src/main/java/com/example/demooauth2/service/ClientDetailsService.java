package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.security.Principal;
import java.util.List;

public interface ClientDetailsService {
    CommandResult createClientDetail(Principal principal,String clientId, List<String> redirectUri);

    ClientDetails loadClientByClientId(String clientId);

    CommandResult updateClientSecret(String clientId, Principal principal);

    CommandResult updateRedirectUri(String clientId, List<String> redirectUri, Principal principal);

    CommandResult removeClientDetail(String clientId, Principal principal);

    CommandResult getClientsByUserId(Principal principal);

    CommandResult getAllClients(String searchString,int sortType, int pageIndex, int pageSize);
}
