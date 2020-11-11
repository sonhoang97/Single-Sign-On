package com.example.demooauth2.service.commons.jwtTokenProvider;

import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.users.UserTokenViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Override
    public String generateToken(UserTokenViewModel userDetails,String clientId, Long JWT_EXPIRATION, String JWT_SECRET) {
        try {
            if (JWT_EXPIRATION == 0 || JWT_SECRET == null || JWT_SECRET.equals("")||clientId==null||clientId.isEmpty()) return null;
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION * 1000);
            return Jwts.builder()
                    .setSubject(userDetails.getId().toString())
                    .claim("user", userDetails)
                    .claim("clientId",clientId)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Integer getUserIdFromJWT(String token) {
        try {
            if (token == null || token.equals("")) return null;
            Claims claims = this.getClaimsBody(token);
            return Integer.parseInt((String) claims.get("sub"));
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, "Expired JWT token");
        }

    }

    @Override
    public UserTokenViewModel getUserFromJWT(String token) {
        try {
            if (token == null || token.equals("")) {
                return null;
            }
            Claims claims = this.getClaimsBody(token);
            ObjectMapper mapper = new ObjectMapper();
            LinkedHashMap<String, ?> para = (LinkedHashMap<String, ?>) claims.get("user");
            try {
                return mapper.convertValue(para, UserTokenViewModel.class);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid Agrument User in Token");
            }
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, "Expired JWT token");
        }
    }

    @Override
    public String getClientIdFromJWT(String token){
        try {
            if (token == null || token.equals("")) return null;
            Claims claims = this.getClaimsBody(token);
            return (String) claims.get("clientId");
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, "Expired JWT token");
        }
    }

    @Override
    public boolean validateToken(String authToken, String JWT_SECRET) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Claims getClaimsBody(String token){
        List<String> splitToken = Arrays.asList(token.split("\\."));
        String unsignedToken = splitToken.get(0) + "." + splitToken.get(1) + ".";
        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        return (Claims) jwt.getBody();
    }
//    public set
}
