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
    private List<String> usernames;

    public RoleViewModel(RoleEntity role) {
        this.usernames = new ArrayList<>();
        this.name = role.getName();
        this.id = role.getId();
        this.permissions = new HashSet<>(role.getPermissions());
        for (UserEntity user: role.getUsers()) {
            usernames.add(user.getUsername());
        }
    }
}

