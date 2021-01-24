package com.example.demooauth2.token;

import com.example.demooauth2.modelEntity.RefreshTokenEntity;
import com.example.demooauth2.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Optional;

public class JwtTokenStoreService extends JwtTokenStore {

    RefreshTokenRepository refreshTokenRepository;

    public JwtTokenStoreService(JwtAccessTokenConverter jwtTokenEnhancer, RefreshTokenRepository refreshTokenRepository) {
        super(jwtTokenEnhancer);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        //TODO delete
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        //  this.readRefreshToken(refreshToken)
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



}
