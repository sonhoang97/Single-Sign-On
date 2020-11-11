package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import com.example.demooauth2.modelEntity.JWTokenEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.tokens.Oauth2Token;
import com.example.demooauth2.modelView.users.UserTokenViewModel;
import com.example.demooauth2.repository.ClientDetailRepository;
import com.example.demooauth2.repository.JWTokenRepository;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.TokenService;
import com.example.demooauth2.service.commons.jwtTokenProvider.JwtTokenProvider;
import com.example.demooauth2.service.commons.Oauth2Authentication;
import com.example.demooauth2.service.commons.TokenRequest;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {
    private final String tokenType = "Bearer";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTokenRepository jwTokenRepository;
    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Oauth2Token createToken(Oauth2Authentication oauth2Authentication) {
        Authentication userAuth = oauth2Authentication.getAuthentication();
        TokenRequest tokenRequest = oauth2Authentication.getTokenRequest();
        if(userAuth==null || !userAuth.isAuthenticated()){
            throw new InvalidGrantException("Could not authenticate");
        }

        String username = userAuth.getName();
        UserTokenViewModel user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Username not found");
        }

        String jwtSecret = jwTokenRepository.getJwtSecret(user.getId(),tokenRequest.getClientDetail().getClientId());
        if(jwtSecret==null|| jwtSecret.isEmpty())
        {
            jwtSecret = this.createJwtSecretAndSave(oauth2Authentication);
        }

        String token = jwtTokenProvider.generateToken(user,tokenRequest.getClientDetail().getClientId(),tokenRequest.getClientDetail().getTokenExpiration(),jwtSecret);
        String refreshToken = jwtTokenProvider.generateToken(user,tokenRequest.getClientDetail().getClientId(),tokenRequest.getClientDetail().getRefreshExpiration(),jwtSecret);
        if(token==null|| refreshToken==null || token.isEmpty() || refreshToken.isEmpty()){
            return null;
        }

        return new Oauth2Token(token,refreshToken,tokenType);
    }

    @Override
    public Oauth2Token createTokenByRefreshToken(TokenRequest tokenRequest){
        String refreshToken = tokenRequest.getParameters().get("refresh_token");
        Integer userId = jwtTokenProvider.getUserIdFromJWT(refreshToken);
        String jwtSecret = jwTokenRepository.getJwtSecret(userId,tokenRequest.getClientDetail().getClientId());

        if(jwtSecret==null|| jwtSecret.isEmpty()){
            throw new InvalidGrantException("Refresh Token has been removed");
        }
        if(!jwtTokenProvider.validateToken(refreshToken,jwtSecret)){
            throw new InvalidGrantException("Expired refresh token");
        }

        UserTokenViewModel user = userRepository.findUserByUserId(userId);
        String token = jwtTokenProvider.generateToken(user,tokenRequest.getClientDetail().getClientId(),tokenRequest.getClientDetail().getTokenExpiration(),jwtSecret);
        if(token==null|| token.isEmpty()){
            return null;
        }

        return new Oauth2Token(token,refreshToken,tokenType);
    }

    private String createJwtSecretAndSave(Oauth2Authentication oauth2Authentication){
        String jwtSecret;
        jwtSecret = UUID.randomUUID().toString();
        jwtSecret = TextCodec.BASE64.encode(jwtSecret);

        Optional<UserEntity> userEntity = userRepository.findByUsername(oauth2Authentication.getAuthentication().getName());
        Optional<ClientDetailEntity> clientDetailEntity = clientDetailRepository.findClientDetailEntitiesByClientId(oauth2Authentication.getTokenRequest().getClientDetail().getClientId());
        if(userEntity.isPresent() && clientDetailEntity.isPresent()){
            JWTokenEntity jwTokenEntity = new JWTokenEntity(jwtSecret,clientDetailEntity.get(),userEntity.get());
            jwTokenRepository.save(jwTokenEntity);
        }

        return jwtSecret;
    }
}
