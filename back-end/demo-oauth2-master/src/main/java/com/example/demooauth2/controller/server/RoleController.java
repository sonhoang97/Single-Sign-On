package com.example.demooauth2.controller.server;

import com.example.demooauth2.model.Role;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.PermissionService;
import com.example.demooauth2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("")
    public ResponseEntity<Object> CreateNew(@RequestBody Role permission) {
        CommandResult result = roleService.CreateNew(permission);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> Update(@PathVariable(value = "id") int roleId,
                                         @RequestBody Role role) {
        CommandResult result = roleService.Update(roleId, role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @GetMapping("")
    public ResponseEntity<Object> GetAll() {
        CommandResult result = roleService.getAll();
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") int roleId) {
        CommandResult result = roleService.Delete(roleId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }
}
