package com.example.demooauth2.modelView.clientDetail;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private List<String> authorizeGrantType;
    private LocalDateTime createdAt;
    private List<String> additionalInformation;
    public ClientDetailViewModel(){}

    public ClientDetailViewModel(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public ClientDetailViewModel(ClientDetailEntity clientDetailEntity){
        this.clientId = clientDetailEntity.getClientId();
        this.clientSecret = clientDetailEntity.getClientSecret();
        this.redirectUri = clientDetailEntity.getRedirectUri();
        this.tokenExpiration = clientDetailEntity.getTokenValid();
        this.refreshExpiration =clientDetailEntity.getRefreshTokenValid();
        this.scope = clientDetailEntity.getScope();
        this.authorizeGrantType = clientDetailEntity.getAuthorizedGrantTypes();
        this.createdAt = clientDetailEntity.getCreatedAt();
        this.additionalInformation = clientDetailEntity.getAdditionalInformation();
    }
    public boolean isValid(){
        return this.clientId != null;
    }
}
