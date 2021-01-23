package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ClientDetailRepository extends JpaRepository<ClientDetailEntity, Integer> {
    Optional<ClientDetailEntity> findClientDetailEntitiesByClientId(String clientId);

    @Query(value ="SELECT NEW com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel(c) FROM ClientDetailEntity c WHERE c.clientId=:client_id")
    <ClientDetailViewModel>
    ClientDetailViewModel findByClientId(String client_id);

    @Query(value ="SELECT NEW com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel(c) FROM ClientDetailEntity c WHERE c.user.id=:userId")
//    <List<ClientDetailViewModel>>
    List<ClientDetailViewModel> getClientsByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query("update ClientDetailEntity set clientSecret=:clientSecret where clientId=:clientId")
    void updateClientSecret(String clientId, String clientSecret);

    @Transactional
    @Modifying
    @Query("update ClientDetailEntity set redirectUri=:redirectUri where clientId=:clientId")
    void updateRedirectUri(String clientId,List<String> redirectUri);

    @Transactional
    @Modifying
    @Query ("delete from ClientDetailEntity where clientId=:clientId")
    void deleteClientDetail(String clientId);

}
