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
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private int phoneNumber;

    public UserTokenViewModel(){};

    public UserTokenViewModel(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.email = userEntity.getEmail();
        this.phoneNumber = userEntity.getPhonenumber();
    }

    public boolean isValid() {
        return username != null;
    }
}
