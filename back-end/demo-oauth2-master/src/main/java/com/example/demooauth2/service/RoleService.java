package com.example.demooauth2.service;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelView.roles.RoleViewModel;
import com.example.demooauth2.responseModel.CommandResult;

import java.util.List;
import java.util.Set;

public interface RoleService {
    CommandResult CreateNew(RoleEntity permission) ;

    CommandResult Update(int id, RoleEntity newRole);

    CommandResult getAll();

    CommandResult GetSingleById(int id);

    CommandResult Delete(int id);

    CommandResult AddPermission(int roleId, int permissionId);

    CommandResult DeletePermission(int roleId, int permissionId);

    CommandResult getAllRoles();

    CommandResult UpdatePermissions(int roleId, RoleViewModel roleViewModel);
}
