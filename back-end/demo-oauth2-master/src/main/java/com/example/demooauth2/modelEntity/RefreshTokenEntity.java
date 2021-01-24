package com.example.demooauth2.modelEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "oauth_refresh_token")
@Getter
@Setter
public class RefreshTokenEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "username")
    private String username;

    @Column(name= "refreshToken")
    private String refreshToken;
}
