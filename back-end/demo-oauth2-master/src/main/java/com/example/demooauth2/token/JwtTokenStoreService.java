package com.example.demooauth2.token;

import com.example.demooauth2.modelEntity.RefreshTokenEntity;
import com.example.demooauth2.repository.RefreshTokenRepository;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Optional;

public class JwtTokenStoreService extends JwtTokenStore {

    RefreshTokenRepository refreshTokenRepository;

    public JwtTokenStoreService(JwtAccessTokenConverter jwtTokenEnhancer, RefreshTokenRepository refreshTokenRepository) {
        super(jwtTokenEnhancer);
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        OAuth2Authentication authentication = super.readAuthenticationForRefreshToken(token);
        refreshTokenRepository.deleteByUsername(authentication.getPrincipal().toString());
    }


    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
      Optional<RefreshTokenEntity> existRfToken = refreshTokenRepository.findByUserName(authentication.getPrincipal().toString());

        if (!existRfToken.isPresent()) {
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setUsername(authentication.getPrincipal().toString());
            refreshTokenEntity.setRefreshToken(refreshToken.toString());
            refreshTokenRepository.save(refreshTokenEntity);
        }
        else {
            existRfToken.get().setRefreshToken(refreshToken.toString());
            refreshTokenRepository.save(existRfToken.get());
        }

    }

    public String getRefreshTokenValue(String username){
       Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByUserName(username);
       if (refreshTokenEntity.isPresent()) {
           return  refreshTokenEntity.get().getRefreshToken();
       }
       else  return  null;
    }



}
