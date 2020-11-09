package com.example.demooauth2.modelEntity;

import com.example.demooauth2.commons.Converter.AuthenticationByteConverter;
import com.example.demooauth2.commons.Converter.StringListConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "oauth_code")
@Getter
@Setter
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="code")
    private String code;

    @Column(name = "authentication")
    @Convert(converter = AuthenticationByteConverter.class)
    private Authentication authentication;
}
