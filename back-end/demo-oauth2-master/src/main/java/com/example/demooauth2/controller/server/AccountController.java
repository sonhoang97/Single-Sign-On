package com.example.demooauth2.controller.server;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.User;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User userDTO, HttpServletRequest request){
        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        CommandResult result = userService.registerNewUserAccount(userDTO);
        return new ResponseEntity<>(result.getMessage(),result.getStatus());
    }
}
