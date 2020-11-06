package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ClientDetailRepository extends JpaRepository<ClientDetailEntity, Integer> {
    @Query("SELECT NEW com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel(c.clientId,c.clientSecret,c.redirectUri) FROM ClientDetailEntity c WHERE c.clientId=:client_id")
    <clientDetailViewModel>
    clientDetailViewModel findByClientId(String client_id);

    @Transactional
    @Modifying
    @Query("update ClientDetailEntity set clientSecret=:clientSecret where clientId=:clientId")
    void updateClientSecret(String clientId, String clientSecret);

    @Transactional
    @Modifying
    @Query("update ClientDetailEntity set redirectUri=:redirectUri where clientId=:clientId")
    void updateRedirectUri(String clientId,String redirectUri);

    @Transactional
    @Modifying
    @Query ("delete from ClientDetailEntity where clientId=:clientId")
    void deleteClientDetail(String clientId);
}
