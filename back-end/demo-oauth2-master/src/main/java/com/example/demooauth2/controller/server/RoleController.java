package com.example.demooauth2.controller.server;

import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Object> CreateNew(@RequestBody RoleEntity role) {
        CommandResult result = roleService.CreateNew(role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")

    public ResponseEntity<Object> Update(@PathVariable(value = "id") int roleId,
                                         @RequestBody RoleEntity role) {
        CommandResult result = roleService.Update(roleId, role);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @GetMapping("")
    public ResponseEntity<Object> GetAll() {
        CommandResult result = roleService.getAll();
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")

    public ResponseEntity<Object> Delete(@PathVariable(value = "id") int roleId) {
        CommandResult result = roleService.Delete(roleId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }
}
