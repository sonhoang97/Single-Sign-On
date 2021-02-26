package com.example.demooauth2.modelEntity;

import com.example.demooauth2.modelView.roles.RoleViewModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private Set<PermissionEntity> permissions;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user", joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<UserEntity> users;

    public RoleEntity() {
        name = "test";
        permissions = new HashSet<>();
        permissions.add(new PermissionEntity());
    }
    public RoleEntity(RoleViewModel newRole) {
        name = newRole.getName();
        permissions = newRole.getPermissions();
    }

    public void addNewPermisison(PermissionEntity permission) {
        this.permissions.add(permission);
    }

    public void deleteAPermission(PermissionEntity permission) {
        this.permissions.remove(permission);
    }

}