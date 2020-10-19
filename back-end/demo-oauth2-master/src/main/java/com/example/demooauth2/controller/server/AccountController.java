package com.example.demooauth2.controller.server;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.Test;
import com.example.demooauth2.model.User;
import com.example.demooauth2.service.UserService;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User userRegister, HttpServletRequest request){
        userRegister.setPassword(new BCryptPasswordEncoder().encode(userRegister.getPassword()));
        CommandResult result = userService.registerNewUserAccount(userRegister);
        return new ResponseEntity<>(result.getMessage(),result.getStatus());
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User userDTO, HttpServletRequest request){
//
//        return new ResponseEntity<>("Login", HttpStatus.OK);
//    }

    @PostMapping("/admin")
    public ResponseEntity<String> main(@RequestBody User userDTO, HttpServletRequest request){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/home")
    public ResponseEntity<Test> home(HttpSession httpSession){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
