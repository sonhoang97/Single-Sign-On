package com.example.demooauth2.service.commons;

import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.users.UserTokenViewModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtTokenProvider {

    public String generateToken(UserTokenViewModel userDetails, Long JWT_EXPIRATION, String JWT_SECRET) {
        try {
            if (JWT_EXPIRATION == 0 || JWT_SECRET == null || JWT_SECRET.equals("")) return null;
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION*1000);
            return Jwts.builder()
                    .setSubject(userDetails.getId().toString())
                    .claim("user", userDetails)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } catch (Exception ex) {
            return null;
        }
    }

    public Integer getUserIdFromJWT(String token) {
        try {
            if (token == null || token.equals("")) return null;
            List<String> splitToken = Arrays.asList(token.split("\\."));
            String unsignedToken = splitToken.get(0) + "." + splitToken.get(1) + ".";
            DefaultJwtParser parser = new DefaultJwtParser();
            Jwt<?, ?> jwt = parser.parse(unsignedToken);
            Claims claims = (Claims) jwt.getBody();

            return Integer.parseInt((String) claims.get("sub"));
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null,null,"Expired JWT token");
        }

    }

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

//    public set
}
