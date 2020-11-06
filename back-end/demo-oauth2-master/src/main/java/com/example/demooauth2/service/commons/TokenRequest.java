package com.example.demooauth2.service.commons;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class TokenRequest {
    private Map<String, String> parameters;
    private String clientId;
    private Collection<String> scopes;
    private String grantType;
    public TokenRequest(){}
    public TokenRequest(Map<String, String> parameters,String clientId,Collection<String> scopes,String grantType){
        this.parameters = parameters;
        this.clientId = clientId;
        this.scopes = scopes;
        this.grantType= grantType;
    }

    public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient){
        String clientId = (String)requestParameters.get("client_id");
        if (clientId == null) {
            clientId = authenticatedClient.getClientId();
        } else if (!clientId.equals(authenticatedClient.getClientId())) {
            throw new InvalidClientException("Given client ID does not match authenticated client");
        }

        String grantType = (String)requestParameters.get("grant_type");
        Set<String> scopes = OAuth2Utils.parseParameterList((String)requestParameters.get("scope"));
        return new TokenRequest(requestParameters, clientId, scopes, grantType);
    }
}
