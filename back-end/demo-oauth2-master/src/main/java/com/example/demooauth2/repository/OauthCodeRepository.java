package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.CodeEntity;
import com.example.demooauth2.modelEntity.JWTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;

import javax.transaction.Transactional;

public interface OauthCodeRepository extends JpaRepository<CodeEntity, Integer> {

    @Query("select c.authentication from CodeEntity c where c.code=:code")
    Authentication getAuthentication(String code);

    @Transactional
    @Modifying
    @Query ("delete from CodeEntity where code=:code")
    void deleteCode(String code);
}
