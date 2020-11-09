package com.example.demooauth2.modelEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "oauth_token")
@Getter
@Setter
public class JWTokenEntity implements Serializable {

    public JWTokenEntity(String jwtSecret,ClientDetailEntity clientDetailEntity, UserEntity userEntity){
        this.jwtSecret = jwtSecret;
        this.clientDetail = clientDetailEntity;
        this.user = userEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "jwt_secret")
    private String jwtSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientDetailEntity clientDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public JWTokenEntity() {

    }
}
