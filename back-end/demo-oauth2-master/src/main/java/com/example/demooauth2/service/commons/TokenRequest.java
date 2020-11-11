package com.example.demooauth2.service.commons;

import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@Getter
@Setter
public class TokenRequest {
    private Map<String, String> parameters;
    private ClientDetailViewModel clientDetail;
    private List<String> scopes;
    private String grantType;

    public TokenRequest(){}

    public TokenRequest(Map<String, String> parameters, ClientDetailViewModel clientDetail, List<String> scopes, String grantType) {
        this.parameters = parameters;
        this.clientDetail = clientDetail;
        this.scopes = scopes;
        this.grantType = grantType;
    }

    public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetailViewModel authenticatedClient) {
        String clientId = (String) requestParameters.get("client_id");
        if (clientId == null) {
            clientId = authenticatedClient.getClientId();
        } else if (!clientId.equals(authenticatedClient.getClientId())) {
            throw new InvalidClientException("Given client ID does not match authenticated client");
        }

        String grantType = (String) requestParameters.get("grant_type");
        List<String> scopes = new ArrayList<String>();
        if (requestParameters.get("scope") != null) {
            scopes = new ArrayList<String>(Arrays.asList((requestParameters.get("scope").split(","))));
        }
        if (scopes.isEmpty()) {
            scopes = authenticatedClient.getScope();
        }
        return new TokenRequest(requestParameters, authenticatedClient, scopes, grantType);
    }
}
