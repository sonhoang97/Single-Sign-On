package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.repository.RoleRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public CommandResult CreateNew(RoleEntity role) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findByName(role.getName());
            if(existRole.isPresent()) {
                return new CommandResult(HttpStatus.CONFLICT, "Role name is duplicate");
            }
            roleRepository.save(role);
            return  new CommandResult().SucceedWithData("Create new role successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Create new role fail!");
        }
    }

    @Override
    public CommandResult Update(int id, RoleEntity newRole) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findById(id);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            roleRepository.save(newRole);
            return  new CommandResult().SucceedWithData("Update role successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Update role fail!");
        }
    }

    @Override
    public CommandResult getAll() {
        try {
            List<RoleEntity> data = roleRepository.findAll();
            return  new CommandResult().SucceedWithData(data);
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Get role fail!");

        }
    }

    @Override
    public CommandResult GetSingleById(int id) {
        try {
           Optional<RoleEntity> role = roleRepository.findById(id);
            return  new CommandResult().SucceedWithData(role);
        }
        catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Get role fail!");

        }
    }

    @Override
    public CommandResult Delete(int id) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findById(id);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            roleRepository.delete(existRole.get());
            return  new CommandResult().SucceedWithData("Delete role successful!");
        }
        catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Get role fail!");

        }
    }
}
