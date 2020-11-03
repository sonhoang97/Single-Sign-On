package com.example.demooauth2.service;

import com.example.demooauth2.model.Permission;
import com.example.demooauth2.responseModel.CommandResult;

public interface PermissionService {
    CommandResult CreateNew(Permission permission) ;

    CommandResult Update(int id, Permission newPermission);

    CommandResult getAll();
    CommandResult Delete(int id);
}
