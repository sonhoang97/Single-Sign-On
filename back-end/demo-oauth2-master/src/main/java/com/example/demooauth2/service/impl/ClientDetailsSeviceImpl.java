package com.example.demooauth2.service.impl;

import com.example.demooauth2.commons.ClientDetailValue;
import com.example.demooauth2.commons.Messages;
import com.example.demooauth2.modelEntity.ClientDetailEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.repository.ClientDetailRepository;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientDetailsSeviceImpl implements ClientDetailsService {

    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailRepository clientDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            return jdbcClientDetailsService.loadClientByClientId(clientId);
        } catch (NoSuchClientException ex) {
            return null;
        }
    }

    @Override
    public CommandResult getClientsByUserId(Principal principal){
        if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
            return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
        }
        Integer userId = userRepository.findIdByUsername(principal.getName());
        if(userId==0){
            return new CommandResult(HttpStatus.NOT_FOUND,"Not found clients!");
        }

        List<ClientDetailViewModel> clients= clientDetailRepository.getClientsByUserId(userId);
        return new CommandResult().SucceedWithData(clients);
    }

    @Override
    public CommandResult createClientDetail(Principal principal,String clientId, String redirectUri) {
        try {
            if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
                return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
            }
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }

            String clientSecret = UUID.randomUUID().toString();
            Optional<UserEntity> user = userRepository.findByUsername(principal.getName());
            if (user.isPresent()) {
                ClientDetailEntity clientDetailEntity = new ClientDetailEntity(clientId, passwordEncoder.encode(clientSecret), redirectUri, user.get());
                clientDetailRepository.save(clientDetailEntity);
                ClientDetailViewModel result = new ClientDetailViewModel(clientId, clientSecret);
                return new CommandResult().SucceedWithData(result);
            }
            return new CommandResult(HttpStatus.NOT_FOUND,"not found user");
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
            clientDetailRepository.updateClientSecret(clientId, this.passwordEncoder.encode(clientSecret));

            return new CommandResult().SucceedWithData(new ClientDetailViewModel(clientId,clientSecret));
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.NOT_FOUND, "No client found with id = " + clientId);
        }
    }

    @Override
    public CommandResult updateRedirectUri(String clientId, String redirectUri) {
        try {
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            clientDetailRepository.updateRedirectUri(clientId,redirectUri);
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.NOT_FOUND, "No client with requested id: " + clientId);
        }
    }

    @Override
    public CommandResult removeClientDetail(String clientId){
        try{
            if (clientId == null || clientId.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            clientDetailRepository.deleteClientDetail(clientId);
            return new CommandResult().Succeed();
        }catch(Exception ex){
            return new CommandResult(HttpStatus.NOT_FOUND, "No client found with id = " + clientId);
        }
    }
}
