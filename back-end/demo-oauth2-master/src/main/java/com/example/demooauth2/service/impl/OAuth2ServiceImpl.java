package com.example.demooauth2.service.impl;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.security.Principal;
import java.util.*;

@Service("OAuth2Service")
public class OAuth2ServiceImpl implements OAuth2Service {
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Override
    public CommandResult revokeToken(String token, String refreshToken) {
//        try {
//            OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(token);
//            OAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(refreshToken);
//            tokenStore.removeAccessToken(oAuth2AccessToken);
//            tokenStore.removeRefreshToken(oAuth2RefreshToken);
//            return new CommandResult().Succeed();
//        } catch (Exception ex) {
//            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return new CommandResult().Succeed();
    }

    @Override
    public CommandResult responseAuthorizationCode(Map<String, String> parameters, Principal principal) {
        AuthorizationRequest authorizationRequest = createAuthorizationRequest(parameters);
        Set<String> responseTypes = authorizationRequest.getResponseTypes();

        if (!(principal instanceof Authentication)) {
            return new CommandResult(HttpStatus.UNAUTHORIZED, "There is no client authentication. Try adding an appropriate authentication filter.");
        }

        if (!responseTypes.contains("code")) {
            return new CommandResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported response types: " + responseTypes);
        }

        if (authorizationRequest.getClientId() == null) {
            return new CommandResult(HttpStatus.NO_CONTENT, "A client id must be provided");
        }

        try {
            if (((Authentication) principal).isAuthenticated()) {
                ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
                String redirectUriParameter = authorizationRequest.getRequestParameters().get("redirect_uri");
                if ( redirectUriParameter==null || redirectUriParameter.isEmpty()){
                    return new CommandResult(HttpStatus.NO_CONTENT, "Redirect Uri must be provided");
                }
                String resolvedRedirect = resolveRedirect(redirectUriParameter, client);
                if (!StringUtils.hasText(resolvedRedirect)) {
                    return new CommandResult(HttpStatus.CONFLICT, "A redirectUri must be either supplied or preconfigured in the ClientDetails");
                }
                authorizationRequest.setRedirectUri(resolvedRedirect);

                validateScope(authorizationRequest.getScope(), client.getScope());
                if (checkApproved(principal, client)) {
                    authorizationRequest.setApproved(true);
                    if (responseTypes.contains("code")) {
                        return new CommandResult(HttpStatus.OK, generateCode(authorizationRequest, (Authentication) principal));
                    }
                    return new CommandResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupport response type");
                }
                return new CommandResult(HttpStatus.OK, "redirect to approved");

            } else {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "User must be authenticated with Spring Security before authorization can be completed.");
            }
        } catch (RuntimeException var11) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, var11.getMessage());
        }
    }

    private boolean checkApproved(Principal principal, ClientDetails clientDetails) {
        Collection<Approval> lsApproval = approvalStore.getApprovals(principal.getName(), clientDetails.getClientId());
        for (Approval approval : lsApproval) if (!approval.isApproved()) return false;
        return true;
    }

    private void validateScope(Set<String> requestScopes, Set<String> clientScopes) {
        if (clientScopes != null && !clientScopes.isEmpty()) {

            for (String scope : requestScopes) {
                if (!clientScopes.contains(scope)) {
                    throw new InvalidScopeException("Invalid scope: " + scope);
                }
            }
        }
        if (requestScopes.isEmpty()) {
            throw new InvalidScopeException("Empty scope (either the client or the user is not allowed the requested scopes)");
        }
    }

    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request =
                    new OAuth2Request(
                            authorizationRequest.getRequestParameters(),
                            authorizationRequest.getClientId(),
                            authorizationRequest.getAuthorities(),
                            authorizationRequest.isApproved(),
                            authorizationRequest.getScope(),
                            authorizationRequest.getResourceIds(),
                            authorizationRequest.getRedirectUri(),
                            authorizationRequest.getResponseTypes(),
                            authorizationRequest.getExtensions()
                    );
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            return authorizationCodeServices.createAuthorizationCode(combinedAuth);
        } catch (OAuth2Exception var6) {
            if (authorizationRequest.getState() != null) {
                var6.addAdditionalInformation("state", authorizationRequest.getState());
            }
            throw var6;
        }
    }

    private String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {
        Set<String> authorizedGrantTypes = client.getAuthorizedGrantTypes();
        if (authorizedGrantTypes.isEmpty()) {
            throw new InvalidGrantException("A client must have at least one authorized grant type.");
        } else {
            Set<String> registeredRedirectUris = client.getRegisteredRedirectUri();
            if (registeredRedirectUris != null && !registeredRedirectUris.isEmpty()) {
                for (String scope : registeredRedirectUris) {
                    if (!requestedRedirect.contains(scope)) {
                        throw new InvalidScopeException("Invalid scope: " + scope);
                    }
                }
                return requestedRedirect;
            } else {
                throw new InvalidRequestException("At least one redirect_uri must be registered with the client.");
            }
        }
    }

    private AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
        String clientId = authorizationParameters.get("client_id");
        String state = authorizationParameters.get("state");
        String redirectUri = authorizationParameters.get("redirect_uri");
        Set<String> responseTypes = OAuth2Utils.parseParameterList(authorizationParameters.get("response_type"));
        Set<String> scopes = this.extractScopes(authorizationParameters, clientId);
        AuthorizationRequest request = new AuthorizationRequest(
                authorizationParameters, Collections.emptyMap(),
                clientId,
                scopes,
                null,
                null,
                false,
                state,
                redirectUri,
                responseTypes
        );
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
        request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);
        return request;
    }

    private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
        Set<String> scopes = OAuth2Utils.parseParameterList(requestParameters.get("scope"));
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
        if (scopes.isEmpty()) {
            scopes = clientDetails.getScope();
        }
        return scopes;
    }

}
