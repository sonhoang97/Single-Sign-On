package com.example.demooauth2.service;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.responseModel.CommandResult;

public interface RoleService {
    CommandResult CreateNew(RoleEntity permission) ;

    CommandResult Update(int id, RoleEntity newRole);

    CommandResult getAll();

    CommandResult GetSingleById(int id);

    CommandResult Delete(int id);

    CommandResult AddPermission(int roleId, int permissionId);

    CommandResult DeletePermission(int roleId, int permissionId);


}
