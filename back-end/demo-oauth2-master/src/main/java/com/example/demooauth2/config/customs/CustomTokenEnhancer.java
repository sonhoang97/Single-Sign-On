package com.example.demooauth2.config.customs;

import com.example.demooauth2.modelView.users.UserTokenViewModel;
import com.example.demooauth2.repository.UserRepository;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {
    private final UserRepository userRepository;

    public CustomTokenEnhancer(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(authentication==null||!authentication.isAuthenticated()){
            return accessToken;
        }
        Map<String, Object> additionalInfo = new HashMap<>();
        UserTokenViewModel user = userRepository.findUserByUsername(authentication.getName());
        additionalInfo.put("user", user);
        additionalInfo.remove("aud");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
