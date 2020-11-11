package com.example.demooauth2.modelView.clientDetail;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ClientDetailViewModel {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private Long tokenExpiration;
    private Long refreshExpiration;
    private List<String> scope;

    public ClientDetailViewModel(){}

    public ClientDetailViewModel(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public ClientDetailViewModel(String clientId, String clientSecret, String redirectUri, Long tokenExpiration, Long refreshExpiration,List<String> scope){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenExpiration = tokenExpiration;
        this.refreshExpiration =refreshExpiration;
        this.scope = scope;
    }

}
