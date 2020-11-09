package com.example.demooauth2.modelView.tokens;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Oauth2Token {
    private String jwtToken;
    private String jwtRefreshToken;
    private String tokenType;

    public Oauth2Token(String jwtToken, String jwtRefreshToken, String tokenType){
        this.jwtToken = jwtToken;
        this.jwtRefreshToken = jwtRefreshToken;
        this.tokenType = tokenType;
    }
}
