package com.example.demooauth2.controller.server;

import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("#oauth2.hasScope('WRITE') AND hasRole('admin')")
    public ResponseEntity listUser() {
        return ResponseEntity.ok(userService.findAll());
    }

}
