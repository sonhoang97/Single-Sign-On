package com.example.demooauth2.modelEntity;

import com.example.demooauth2.commons.ClientDetailValue;
import com.example.demooauth2.commons.Converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "oauth_client_details")
@Getter
@Setter
public class ClientDetailEntity implements Serializable {

    public ClientDetailEntity() {

    }
    public ClientDetailEntity(String clientId, String clientSecret,List<String> redirectUri,UserEntity user){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = ClientDetailValue.SCOPE_DEFAULT;
        this.tokenValid = (long)ClientDetailValue.TOKEN_VALIDITY_SECONDS;
        this.refreshTokenValid = (long)ClientDetailValue.REFRESH_TOKEN_VALIDITY_SECONDS;
        this.resourceIds = ClientDetailValue.RESOURCE_ID;
        this.authorizedGrantTypes = ClientDetailValue.AUTHORIZE_DEFAULT;
        this.user = user;
    }

    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "web_server_redirect_uri")
    @Convert(converter = StringListConverter.class)
    private List<String> redirectUri;

    @Column(name = "scope")
    @Convert(converter = StringListConverter.class)
    private List<String> scope;

    @Column(name = "access_token_validity")
    private Long tokenValid;

    @Column(name = "refresh_token_validity")
    private Long refreshTokenValid;

    @Column(name = "resource_ids")
    @Convert(converter = StringListConverter.class)
    private List<String> resourceIds;

    @Column(name = "authorized_grant_types")
    @Convert(converter = StringListConverter.class)
    private List<String> authorizedGrantTypes;

    @Column(name = "authorities")
    @Convert(converter = StringListConverter.class)
    private List<String> authorities;

    @Column(name = "additional_information")
    @Convert(converter = StringListConverter.class)
    private List<String> additionalInformation;

    @Column(name = "autoapprove")
    @Convert(converter = StringListConverter.class)
    private List<String> autoApprove;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
