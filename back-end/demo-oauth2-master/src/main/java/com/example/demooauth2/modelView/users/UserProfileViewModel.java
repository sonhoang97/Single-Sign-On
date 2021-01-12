package com.example.demooauth2.modelView.users;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserProfileViewModel {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private List<ClientDetailViewModel> lsClientDetail = new ArrayList<>();
    private Set<RoleEntity> lsRoles = new HashSet<>();

    public UserProfileViewModel() {
    }

    public UserProfileViewModel(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.email = userEntity.getEmail();
        this.phonenumber = userEntity.getPhonenumber();
        this.lsRoles = userEntity.getRoles();
        for (ClientDetailEntity clientDetail : userEntity.getClients()) {
            lsClientDetail.add(new ClientDetailViewModel(clientDetail));
        }
    }

    public boolean isValid() {
        return username != null;
    }
}
