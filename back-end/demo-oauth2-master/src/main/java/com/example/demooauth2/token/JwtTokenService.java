package com.example.demooauth2.token;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
//
//RefreshTokenRepository refreshTokenRepository;
//
//public JwtTokenService(RefreshTokenRepository refreshTokenRepository) {
//        this.refreshTokenRepository = refreshTokenRepository;
//        }
//
//@Override
//public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
//
//        Optional<RefreshTokenEntity> existRefreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue);
//        if (existRefreshToken.isPresent()){
//        return super.refreshAccessToken(refreshTokenValue, tokenRequest);
//        }
//        else throw new InvalidTokenException("Can not find refresh token");
//        }
//
//@Override
//@Transactional
//public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
//        //TODO get rftoken by authentication
//        //if exist -> super createAccessToken(OAuth2Authentication authentication, refresh token)
//        // else super createAccessToken(authentication)
//        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
//        Optional<RefreshTokenEntity> existRefreshToken = refreshTokenRepository.findByUserName(authentication.getPrincipal().toString());
//
//        if (existRefreshToken.isPresent()) {
//        token.setRefreshToken(existRefreshToken.get().getRefreshToken());
//        token.setScope(authentication.getOAuth2Request().getScope());
//        return (OAuth2AccessToken)(this.accessTokenEnhancer != null ? this.accessTokenEnhancer.enhance(token, authentication) : token);
//
//        }
//        else  {
//
//        }
//        }


public class JwtTokenService implements AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices, InitializingBean {
    private int refreshTokenValiditySeconds = 2592000;
    private int accessTokenValiditySeconds = 43200;
    private boolean supportRefreshToken = false;
    private boolean reuseRefreshToken = true;
    private JwtTokenStoreService tokenStore;
    private ClientDetailsService clientDetailsService;
    private TokenEnhancer accessTokenEnhancer;
    private AuthenticationManager authenticationManager;

    public JwtTokenService() {

    }
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.tokenStore, "tokenStore must be set");
    }

    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken existingAccessToken = this.tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (!existingAccessToken.isExpired()) {
                this.tokenStore.storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }

            if (existingAccessToken.getRefreshToken() != null) {
                refreshToken = existingAccessToken.getRefreshToken();
                this.tokenStore.removeRefreshToken(refreshToken);
            }

            this.tokenStore.removeAccessToken(existingAccessToken);
        }

        //Find refresh token in store
        String refreshTokenValue =  this.tokenStore.getRefreshTokenValue(authentication.getPrincipal().toString());
        if (refreshTokenValue != null) {
            refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        }

        if (refreshToken == null) {
            refreshToken = this.createRefreshToken(authentication);
        } else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken)refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = this.createRefreshToken(authentication);
            }
        }

        OAuth2AccessToken accessToken = this.createAccessToken(authentication, refreshToken);

        this.tokenStore.storeAccessToken(accessToken, authentication);
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            this.tokenStore.storeRefreshToken(refreshToken, authentication);
        }

        return accessToken;
    }

    @Transactional(
            noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class}
    )
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        JsonParser objectMapper = JsonParserFactory.create();

        String jtiRequest = "1";
        String existJti ="2";
        try {
            Map<String, Object> claimsRequest = objectMapper.parseMap(JwtHelper.decode(refreshTokenValue).getClaims());
            if (claimsRequest.containsKey("jti")) {
                jtiRequest = claimsRequest.get("jti").toString();
            }
        } catch (IllegalArgumentException var11) {}

        Authentication authenticationRequest = tokenStore.readAuthentication(refreshTokenValue);
        String existRefreshTokenValue = tokenStore.getRefreshTokenValue(authenticationRequest.getPrincipal().toString());

        try {
            Map<String, Object> exitstClaims = objectMapper.parseMap(JwtHelper.decode(existRefreshTokenValue).getClaims());
            if (exitstClaims.containsKey("jti")) {
                existJti = exitstClaims.get("jti").toString();
            }
        } catch (IllegalArgumentException var11) {}

        if (!jtiRequest.equals(existJti) ) {
            throw new InvalidTokenException("Refresh token is invalid");
        }

        if (!this.supportRefreshToken) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        } else {
            OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
            if (refreshToken == null) {
                throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
            } else {
                OAuth2Authentication authentication = this.tokenStore.readAuthenticationForRefreshToken(refreshToken);
                if (this.authenticationManager != null && !authentication.isClientOnly()) {
                    Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
                    user = this.authenticationManager.authenticate(user);
                    Object details = authentication.getDetails();
                    authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
                    authentication.setDetails(details);
                }

                String clientId = authentication.getOAuth2Request().getClientId();
                if (clientId != null && clientId.equals(tokenRequest.getClientId())) {
                    this.tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
                    if (this.isExpired(refreshToken)) {
                        this.tokenStore.removeRefreshToken(refreshToken);
                        throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
                    } else {
                        authentication = this.createRefreshedAuthentication(authentication, tokenRequest);
                        if (!this.reuseRefreshToken) {
                            this.tokenStore.removeRefreshToken(refreshToken);
                            refreshToken = this.createRefreshToken(authentication);
                        }
// no su dung rf token cu why ????
                        OAuth2AccessToken accessToken = this.createAccessToken(authentication, refreshToken);
                        this.tokenStore.storeAccessToken(accessToken, authentication);
                        if (!this.reuseRefreshToken) {
                            this.tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
                        }

                        return accessToken;
                    }
                } else {
                    throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
                }
            }
        }
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return this.tokenStore.getAccessToken(authentication);
    }

    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope + ".", originalScope);
            }

            clientAuth = clientAuth.narrowScope(scope);
        }

        OAuth2Authentication narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
        return narrowed;
    }

    protected boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (!(refreshToken instanceof ExpiringOAuth2RefreshToken)) {
            return false;
        } else {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken)refreshToken;
            return expiringToken.getExpiration() == null || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        return this.tokenStore.readAccessToken(accessToken);
    }

    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            this.tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        } else {
            OAuth2Authentication result = this.tokenStore.readAuthentication(accessToken);
            if (result == null) {
                throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
            } else {
                if (this.clientDetailsService != null) {
                    String clientId = result.getOAuth2Request().getClientId();

                    try {
                        this.clientDetailsService.loadClientByClientId(clientId);
                    } catch (ClientRegistrationException var6) {
                        throw new InvalidTokenException("Client not valid: " + clientId, var6);
                    }
                }

                return result;
            }
        }
    }

    public String getClientId(String tokenValue) {
        OAuth2Authentication authentication = this.tokenStore.readAuthentication(tokenValue);
        if (authentication == null) {
            throw new InvalidTokenException("Invalid access token: " + tokenValue);
        } else {
            OAuth2Request clientAuth = authentication.getOAuth2Request();
            if (clientAuth == null) {
                throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
            } else {
                return clientAuth.getClientId();
            }
        }
    }

    public boolean revokeToken(String tokenValue) {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return false;
        } else {
            if (accessToken.getRefreshToken() != null) {
                this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }

            this.tokenStore.removeAccessToken(accessToken);
            return true;
        }
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!this.isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        } else {
            int validitySeconds = this.getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
            String value = UUID.randomUUID().toString();
            return (OAuth2RefreshToken)(validitySeconds > 0 ? new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis() + (long)validitySeconds * 1000L)) : new DefaultOAuth2RefreshToken(value));
        }

    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = this.getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (long)validitySeconds * 1000L));
        }

        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());
        if (this.accessTokenEnhancer != null){
            token = (DefaultOAuth2AccessToken) this.accessTokenEnhancer.enhance(token, authentication);
        }
        return (OAuth2AccessToken) token;
    }

    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }

        return this.accessTokenValiditySeconds;
    }

    protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }

        return this.refreshTokenValiditySeconds;
    }

    protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            return client.getAuthorizedGrantTypes().contains("refresh_token");
        } else {
            return this.supportRefreshToken;
        }
    }

    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public void setSupportRefreshToken(boolean supportRefreshToken) {
        this.supportRefreshToken = supportRefreshToken;
    }

    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        this.reuseRefreshToken = reuseRefreshToken;
    }

    public void setTokenStore(JwtTokenStoreService tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }
}
