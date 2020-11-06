package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
    Optional<PermissionEntity> findByName(String permissionName);

}
