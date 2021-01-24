package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    @Query("select r from RefreshTokenEntity r where r.username = :username")
    Optional<RefreshTokenEntity> findByUserName(String username);
}