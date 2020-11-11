package com.example.demooauth2.service.commons.jwtTokenProvider;

import com.example.demooauth2.modelView.users.UserTokenViewModel;

public interface JwtTokenProvider {
    String generateToken(UserTokenViewModel userDetails,String clientId, Long JWT_EXPIRATION, String JWT_SECRET);
    Integer getUserIdFromJWT(String token);
    UserTokenViewModel getUserFromJWT(String token);
    String getClientIdFromJWT(String token);
    boolean validateToken(String authToken, String JWT_SECRET);

}
