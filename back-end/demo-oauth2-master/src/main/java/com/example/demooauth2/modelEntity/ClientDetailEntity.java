package com.example.demooauth2.modelEntity;

import com.example.demooauth2.commons.Converter.StringListConverter;
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
    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "web_server_redirect_uri")
    private String redirectUri;

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
}
