package com.example.demooauth2.service.impl;

import com.example.demooauth2.model.Permission;
import com.example.demooauth2.repository.PermissionRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public CommandResult CreateNew(Permission permission) {
        try {
            Optional<Permission> per = permissionRepository.findByName(permission.getName());
            if (per.isPresent())
                return new CommandResult(HttpStatus.CONFLICT, "Permission has existed!");

            permissionRepository.save(permission);
            return new CommandResult().SucceedWithData("Create new role successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.BAD_REQUEST, "Cannot create new permission!");
        }
    }

    @Override
    public CommandResult Update(int id, Permission newPermission) {
        try {
            if (!permissionRepository.findById(id).isPresent())
                return new CommandResult(HttpStatus.NOT_FOUND, "Cannot find permission!");

            permissionRepository.save(newPermission);
            return new CommandResult().SucceedWithData("Update permission successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.BAD_REQUEST, "Cannot update permission!");
        }
    }

    @Override
    public CommandResult getAll() {
        try {
            List<Permission> data = permissionRepository.findAll();
            return new CommandResult().SucceedWithData(data);
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.BAD_REQUEST, "Cannot get all permission!");
        }
    }

    @Override
    public CommandResult Delete(int id) {
        try {
            Optional<Permission> permission = permissionRepository.findById(id);
            if (!permission.isPresent())
                return new CommandResult(HttpStatus.NOT_FOUND, "Cannot find permission!");

            permissionRepository.deleteById(id);
            return new CommandResult().SucceedWithData("Delete permission successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.BAD_REQUEST, "Cannot delete permission!");
        }
    }
}
