package com.example.demooauth2.modelView.clientDetail;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ClientDetailViewModel {
    private String clientId;
    private String clientSecret;
    private List<String> redirectUri;
    private Long tokenExpiration;
    private Long refreshExpiration;
    private List<String> scope;

    public ClientDetailViewModel(){}

    public ClientDetailViewModel(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public ClientDetailViewModel(ClientDetailEntity clientDetailEntity){
        this.clientId = clientDetailEntity.getClientId();
        this.redirectUri = clientDetailEntity.getRedirectUri();
        this.tokenExpiration = clientDetailEntity.getTokenValid();
        this.refreshExpiration =clientDetailEntity.getRefreshTokenValid();
        this.scope = clientDetailEntity.getScope();
    }
    public boolean isValid(){
        return this.clientId != null;
    }
}
