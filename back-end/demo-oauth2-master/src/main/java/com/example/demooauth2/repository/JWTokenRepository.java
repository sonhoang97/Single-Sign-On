package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.JWTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTokenRepository extends JpaRepository<JWTokenEntity, Integer> {
}
