package com.example.demooauth2.repository;

import com.example.demooauth2.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select r from Role r where r.name =?1")
    Optional<Role> findByName(String roleName);


}
