package com.example.demooauth2.repository;

import com.example.demooauth2.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String permissionName);

}
