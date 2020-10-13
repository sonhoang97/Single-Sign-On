package com.example.demooauth2.controller.server;

import com.example.demooauth2.model.User;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User userDTO, HttpServletRequest request){
        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        userService.registerNewUserAccount(userDTO);
        return "ok";
    }
}
