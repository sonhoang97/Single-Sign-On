package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.JWTokenEntity;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

public interface JWTokenRepository extends JpaRepository<JWTokenEntity, Integer> {
    @Query("select j.jwtSecret from JWTokenEntity j where j.user.id=:idUser and j.clientDetail.clientId=:clientId")
    String getJwtSecret(Integer idUser, String clientId);

    Optional<JWTokenEntity> getById(Integer id);
//    @Transactional
//    @Modifying
//    @Query(value = "insert into JWTokenEntity (jwt_secret,client_id,user_id) VALUES (:jwtSecret,:clientId,:userId)", nativeQuery = true)
//    void logURI(String jwtSecret,String clientId, Long userId);
}
