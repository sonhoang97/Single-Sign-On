package com.example.demooauth2.modelView.users;

import com.example.demooauth2.modelEntity.ClientDetailEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserProfileViewModel {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private List<ClientDetailViewModel> lsClientDetail = new ArrayList<>();

    public UserProfileViewModel() {
    }

    public UserProfileViewModel(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.email = userEntity.getEmail();
        this.phonenumber = userEntity.getPhonenumber();

        for (ClientDetailEntity clientDetail : userEntity.getClients()) {
            lsClientDetail.add(new ClientDetailViewModel(clientDetail));

        }
    }

    public boolean isValid() {
        return username != null;
    }
}
