package com.example.demooauth2.modelEntity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permission")
@Data
public class PermissionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    public PermissionEntity( String name) {
        this.name = name;
    }

    public PermissionEntity() {
    }
}