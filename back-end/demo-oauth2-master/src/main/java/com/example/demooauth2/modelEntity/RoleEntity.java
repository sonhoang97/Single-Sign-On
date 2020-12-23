package com.example.demooauth2.modelEntity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<PermissionEntity> permissions;

    public RoleEntity() {
        name= "test";
        permissions = new ArrayList<PermissionEntity>();
        permissions.add(new PermissionEntity());
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public  void addNewPermisison(PermissionEntity permission) {
        this.permissions.add(permission);
    }

    public  void deleteAPermission(PermissionEntity permission) {
        this.permissions.remove(permission);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}