package com.example.demooauth2.controller.server;

import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.modelView.users.UserTokenViewModel;
import com.example.demooauth2.repository.ClientDetailRepository;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.UserService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private OAuth2Service oAuth2Service;
    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserEntity userRegister){
        userRegister.setPassword(new BCryptPasswordEncoder().encode(userRegister.getPassword()));
        CommandResult result = userService.registerNewUserAccount(userRegister);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @DeleteMapping("/log_out")
    public ResponseEntity<Object> revokeToken(@RequestParam Map<String, Object> requestParam,@RequestHeader (name="Authorization") String token) {
        String refreshToken = (String) requestParam.get("refresh_token");
        String[] tok = token.split(" ");
        CommandResult result = oAuth2Service.revokeToken(tok[1],refreshToken);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(Principal principal){
        CommandResult result = userService.getProfile(principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

   @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(Principal principal,@RequestBody Map<String, String> bodyProfile){
        CommandResult result = userService.updateProfile(principal,bodyProfile);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(Principal principal,@RequestBody Map<String, String> bodyPassword){
        CommandResult result = userService.changePassword(principal,bodyPassword);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }
}
