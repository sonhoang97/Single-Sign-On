package com.example.demooauth2.modelView.clientDetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDetailViewModel {
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public ClientDetailViewModel(){}
    public ClientDetailViewModel(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
    public ClientDetailViewModel(String clientId, String clientSecret, String redirectUri){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
