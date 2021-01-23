package com.example.demooauth2.controller.server;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
   @Autowired
   private PermissionService permissionService;

  @PostMapping("" )
  @PreAuthorize("hasRole('admin')")

  public ResponseEntity<Object> CreateNew(@RequestBody PermissionEntity permissionEntity) {
      CommandResult result = permissionService.CreateNew(permissionEntity);
      return new ResponseEntity<>(result.getData(),result.getStatus());
  }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")

    public ResponseEntity<Object> Update(@PathVariable (value = "id") int permissionId,
                                         @RequestBody PermissionEntity newPermissionEntity) {
        CommandResult result = permissionService.Update(permissionId, newPermissionEntity);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('read_permission')")
    public ResponseEntity<Object> GetAll(){
      CommandResult result = permissionService.getAll();
      return  new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Object> Delete(@PathVariable (value = "id" )int permissionId) {
        CommandResult result = permissionService.Delete(permissionId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }
}
