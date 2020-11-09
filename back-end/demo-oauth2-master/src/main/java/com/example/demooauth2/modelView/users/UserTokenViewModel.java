package com.example.demooauth2.modelView.users;

import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserTokenViewModel {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private int phoneNumber;
    private Set<String> authorities;

    public UserTokenViewModel(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.email = userEntity.getEmail();
        this.phoneNumber = userEntity.getPhonenumber();

        this.authorities = new HashSet<>();
        userEntity.getRoles().forEach(role -> {
            this.authorities.add(role.getName());
            role.getPermissions().forEach(permission -> {
                this.authorities.add(permission.getName());
            });
        });
    }
}
