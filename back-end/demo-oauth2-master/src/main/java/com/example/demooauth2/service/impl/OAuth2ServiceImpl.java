package com.example.demooauth2.service.impl;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.AccessToken;
import com.example.demooauth2.service.OAuth2Service;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("OAuth2Service")
public class OAuth2ServiceImpl implements OAuth2Service {

    @Autowired
    private TokenStore tokenStore;

//    @Override
//    public AccessToken getAccessToken(String clientId, String clientSecret, String redirectUri, String code, String state) {
//        String getToken = "http://localhost:8081/oauth/token?redirect_uri=" + redirectUri + "&grant_type=authorization_code&code=" +
//                code + "&state=" + state;
//        HttpHeaders headers = new HttpHeaders();
//        String base64Cred = Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes());
//        headers.set("Authorization", "Basic " + base64Cred);
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        HttpEntity request = new HttpEntity(headers);
//        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange(getToken, HttpMethod.POST, request, AccessToken.class);
//        return responseEntity.getBody();
//    }

    @Override
    public CommandResult revokeToken(String token, String refreshToken) {
        try {
            OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(token);
            OAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(refreshToken);
            tokenStore.removeAccessToken(oAuth2AccessToken);
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
