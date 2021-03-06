package com.example.demooauth2.controller.server;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelView.roles.RoleViewModel;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('edit_role')")
    public ResponseEntity<Object> CreateNew(@RequestBody RoleViewModel role) {
        CommandResult result = roleService.CreateNew(role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('edit_role')")

    public ResponseEntity<Object> Update(@PathVariable(value = "id") int roleId,
                                         @RequestBody RoleEntity role) {
        CommandResult result = roleService.Update(roleId, role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('read_role')")
    public ResponseEntity<Object> GetAll() {
        CommandResult result = roleService.getAll();
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('read_role')")
    public ResponseEntity<Object> GetAllRoles() {
        CommandResult result = roleService.getAllRoles();
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edit_role')")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") int roleId) {
        CommandResult result = roleService.Delete(roleId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('edit_permission')")
    public ResponseEntity<Object> UpdatePermission(@PathVariable(value = "id") int roleId,
                                                   @RequestBody List<PermissionEntity> permissions) {
        // TODO update new permission for role

        CommandResult result = new CommandResult();
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PostMapping("/{id}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('edit_permission_role')")
    public ResponseEntity<Object> AddNewPermission(@PathVariable(value = "id") int roleId, @PathVariable(value = "permissionId") int permissionId) {

        CommandResult result = roleService.AddPermission(roleId, permissionId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @DeleteMapping("/{id}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('edit_permission_role')")
    public ResponseEntity<Object> RemovePermission(@PathVariable(value = "id") int roleId, @PathVariable(value = "permissionId") int permissionId) {
        CommandResult result = roleService.DeletePermission(roleId, permissionId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PostMapping("/{id}/updatePermissions")
    @PreAuthorize("hasAuthority('edit_role')")
    public ResponseEntity<Object> updatePermissions(@PathVariable(value = "id") int roleId,@RequestBody RoleViewModel role) {
        CommandResult result = roleService.UpdatePermissions(roleId, role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }
}
