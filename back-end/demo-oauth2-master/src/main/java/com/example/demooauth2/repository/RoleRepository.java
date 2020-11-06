package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String roleName);
}
