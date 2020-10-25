package com.example.demooauth2.controller.server;

import com.example.demooauth2.exception.CommandResult;
import com.example.demooauth2.model.User;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.UserService;
import com.example.demooauth2.service.impl.ClientDetailsSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User userRegister){
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
}
