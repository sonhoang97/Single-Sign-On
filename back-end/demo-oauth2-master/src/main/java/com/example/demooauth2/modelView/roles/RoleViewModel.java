package com.example.demooauth2.modelView.roles;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RoleViewModel {
    private String name;
    private Integer id;
    private Set<PermissionEntity> permissions;

    public RoleViewModel(RoleEntity role) {
        this.name = role.getName();
        this.id = role.getId();
        this.permissions = new HashSet<>(role.getPermissions());
    }
}

