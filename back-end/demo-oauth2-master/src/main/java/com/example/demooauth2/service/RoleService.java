package com.example.demooauth2.service;

import com.example.demooauth2.model.Role;
import com.example.demooauth2.responseModel.CommandResult;

public interface RoleService {
    CommandResult CreateNew(Role permission) ;

    CommandResult Update(int id, Role newRole);

    CommandResult getAll();

    CommandResult GetSingleById(int id);

    CommandResult Delete(int id);
}
