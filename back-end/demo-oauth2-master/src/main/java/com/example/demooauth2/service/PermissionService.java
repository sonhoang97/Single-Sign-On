package com.example.demooauth2.service;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.responseModel.CommandResult;

public interface PermissionService {
    CommandResult CreateNew(PermissionEntity permissionEntity) ;

    CommandResult Update(int id, PermissionEntity newPermissionEntity);

    CommandResult getAll();
    CommandResult Delete(int id);
}
