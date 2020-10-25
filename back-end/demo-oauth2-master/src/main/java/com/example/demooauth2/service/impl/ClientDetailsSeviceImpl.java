package com.example.demooauth2.service.impl;

import com.example.demooauth2.commons.ClientDetailValue;
import com.example.demooauth2.commons.Messages;
import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ClientDetailsSeviceImpl implements ClientDetailsService {

    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            return jdbcClientDetailsService.loadClientByClientId(clientId);
        } catch (NoSuchClientException ex) {
            return null;
        }
    }

    @Override
    public CommandResult createClientDetail(String clientId, String redirectUri) {
        try {
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            BaseClientDetails clientDetails = new BaseClientDetails(clientId, ClientDetailValue.RESOURCE_ID, ClientDetailValue.SCOPE_DEFAULT, null, null, redirectUri);

            String clientSecret = UUID.randomUUID().toString();
            clientDetails.setClientSecret(passwordEncoder.encode(clientSecret));
            clientDetails.setAccessTokenValiditySeconds(ClientDetailValue.TOKEN_VALIDITY_SECONDS);
            clientDetails.setRefreshTokenValiditySeconds(ClientDetailValue.REFRESH_TOKEN_VALIDITY_SECONDS);
            jdbcClientDetailsService.addClientDetails(clientDetails);

            //Hash clientId:clientSecret to response to client
            String hashClient = Base64.encodeBase64String((clientDetails.getClientId() + ":" + clientSecret).getBytes());
            return new CommandResult().SucceedWithData(hashClient);

        } catch (ClientAlreadyExistsException ex) {
            return new CommandResult(HttpStatus.CONFLICT, "Client already exists: " + clientId);
        }
    }

    @Override
    public CommandResult updateClientSecret(String clientId) {
        try {
            if (clientId == null || clientId.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            String clientSecret = UUID.randomUUID().toString();
            jdbcClientDetailsService.updateClientSecret(clientId, clientSecret);

            String hashClient = Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes());
            return new CommandResult().SucceedWithData(hashClient);
        } catch (NoSuchClientException ex) {
            return new CommandResult(HttpStatus.NOT_FOUND, "No client found with id = " + clientId);
        }
    }

    @Override
    public CommandResult updateRedirectUri(String clientId, String redirectUri) {
        try {
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            loadClientByClientId(clientId);
            String updateQuery = "update oauth_client_details set web_server_redirect_uri = ? where client_id = ?";
            jdbcTemplateObject.update(updateQuery, redirectUri, clientId);
            return new CommandResult().Succeed();
        } catch (NoSuchClientException ex) {
            return new CommandResult(HttpStatus.NOT_FOUND, "No client with requested id: " + clientId);
        }
    }

    @Override
    public CommandResult removeClientDetail(String clientId){
        try{
            if (clientId == null || clientId.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            jdbcClientDetailsService.removeClientDetails(clientId);
            return new CommandResult().Succeed();
        }catch(NoSuchClientException ex){
            return new CommandResult(HttpStatus.NOT_FOUND, "No client found with id = " + clientId);
        }
    }
}
