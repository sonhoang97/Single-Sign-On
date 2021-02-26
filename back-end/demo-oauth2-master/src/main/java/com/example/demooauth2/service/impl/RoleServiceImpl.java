package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelView.roles.RoleViewModel;
import com.example.demooauth2.repository.PermissionRepository;
import com.example.demooauth2.repository.RoleRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Override
    public CommandResult CreateNew(RoleViewModel role) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findByName(role.getName());
            if(existRole.isPresent()) {
                return new CommandResult(HttpStatus.CONFLICT, "Role name is duplicate");
            }
            RoleEntity newRole = new RoleEntity(role);
            roleRepository.save(newRole);
            return  new CommandResult().Succeed();
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
            if(existRole.get().getName().equals("ROLE_admin") || existRole.get().getId()==1)
            roleRepository.delete(existRole.get());
            return  new CommandResult().SucceedWithData("Delete role successful!");
        }
        catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Get role fail!");

        }
    }

    @Override
    public CommandResult getAllRoles() {
        try {
            List<RoleViewModel> data = roleRepository.getAllRoles();
            return  new CommandResult().SucceedWithData(data);
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Get role fail!");

        }
    }

    public CommandResult AddPermission(int roleId, int permissionId) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            PermissionEntity permission = permissionRepository.getOne(permissionId);
            existRole.get().addNewPermisison(permission);

            roleRepository.save(existRole.get());
            return  new CommandResult().SucceedWithData("Add permission for role successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Add new permission for role fail!");
        }
    }

    public CommandResult DeletePermission(int roleId, int permissionId) {
        try {
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            PermissionEntity permission = permissionRepository.getOne(permissionId);
            existRole.get().deleteAPermission(permission);
            roleRepository.save(existRole.get());
            return new CommandResult().SucceedWithData("Remove permission for role successful!");
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Remove permission for role fail!");
        }
    }

    @Override
    public CommandResult UpdatePermissions(int roleId, RoleViewModel roleViewModel){
        try{
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            existRole.get().setPermissions(roleViewModel.getPermissions());
            roleRepository.save(existRole.get());
            return new CommandResult().Succeed();
        }catch(Exception ex){
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Update permission for role fail!");
        }
    }
}
