package com.example.demooauth2.model.clientDetailViewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDetailViewModel {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    public ClientDetailViewModel(){}
    public ClientDetailViewModel(String clientId, String clientSecret, String redirectUri){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

}
