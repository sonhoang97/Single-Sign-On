package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    @Query("select r from RoleEntity r where r.name =:roleName")
    Optional<RoleEntity> findByName(String roleName);
}
