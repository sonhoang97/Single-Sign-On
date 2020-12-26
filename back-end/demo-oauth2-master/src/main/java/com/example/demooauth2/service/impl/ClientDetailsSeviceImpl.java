package com.example.demooauth2.service.impl;

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
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public CommandResult createClientDetail(Principal principal,String clientId, List<String> redirectUri) {
        try {
            if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
                return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
            }
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            ClientDetailViewModel clientDetailViewModel = clientDetailRepository.findByClientId(clientId);
            if(clientDetailViewModel!=null){
                return new CommandResult(HttpStatus.CONFLICT, "Client already exists: " + clientId);
            }
            String clientSecret = UUID.randomUUID().toString();
            Optional<UserEntity> user = userRepository.findByUsername(principal.getName());
            if (user.isPresent()) {
                ClientDetailEntity clientDetailEntity = new ClientDetailEntity(clientId, passwordEncoder.encode(clientSecret), LocalDateTime.now(), redirectUri, user.get(),clientSecret);
                clientDetailRepository.save(clientDetailEntity);
                return new CommandResult().Succeed();
            }
            return new CommandResult(HttpStatus.NOT_FOUND,"not found user");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong");
        }
    }

    @Override
    public CommandResult updateClientSecret(String clientId, Principal principal) {
        try {
            if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
                return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
            }
            if (clientId == null || clientId.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            Optional<ClientDetailEntity> clientDetail = clientDetailRepository.findClientDetailEntitiesByClientId(clientId);
            if(!clientDetail.isPresent()){
                return new CommandResult(HttpStatus.NOT_FOUND, "Client not found: " + clientId);
            }
            String clientSecret = UUID.randomUUID().toString();
            List<String> additionalInformation = clientDetail.get().getAdditionalInformation();
            if(additionalInformation==null|| additionalInformation.isEmpty()){
                additionalInformation = new ArrayList<>(){
                    {
                        add(clientSecret);
                    }
                };
            } else{
                additionalInformation.set(0,clientSecret);
            }

            clientDetail.get().setClientSecret(this.passwordEncoder.encode(clientSecret));
            clientDetail.get().setAdditionalInformation(additionalInformation);
            clientDetailRepository.save(clientDetail.get());
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!");

        }
    }

    @Override
    public CommandResult updateRedirectUri(String clientId, List<String> redirectUri, Principal principal) {
        try {
            if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
                return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
            }
            if (clientId == null || clientId.isEmpty() || redirectUri == null || redirectUri.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            Optional<ClientDetailEntity> clientDetail = clientDetailRepository.findClientDetailEntitiesByClientId(clientId);
            if(!clientDetail.isPresent()){
                return new CommandResult(HttpStatus.NOT_FOUND, "Client not found: " + clientId);
            }
            clientDetail.get().setRedirectUri(redirectUri);
            clientDetailRepository.save(clientDetail.get());
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!");
        }
    }

    @Override
    public CommandResult removeClientDetail(String clientId, Principal principal){
        try{
            if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
                return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
            }
            if (clientId == null || clientId.isEmpty()) {
                return new CommandResult(HttpStatus.NO_CONTENT, Messages.NO_CONTENT);
            }
            ClientDetailViewModel clientDetailViewModel = clientDetailRepository.findByClientId(clientId);
            if(clientDetailViewModel==null){
                return new CommandResult(HttpStatus.NOT_FOUND, "Client not found: " + clientId);
            }
            clientDetailRepository.deleteClientDetail(clientId);
            return new CommandResult().Succeed();
        }catch(Exception ex){
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong!");

        }
    }
}
