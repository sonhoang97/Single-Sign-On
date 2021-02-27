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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserEntity userRegister){
        userRegister.setPassword(new BCryptPasswordEncoder().encode(userRegister.getPassword()));
        CommandResult result = userService.registerNewUserAccount(userRegister);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(Principal principal){
        CommandResult result = userService.getProfile(principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('read_user')")
    public ResponseEntity<Object> getUser(@RequestParam(defaultValue = "") String username){
        CommandResult result = userService.getUser(username);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/getUsers/{sortType}/{pageIndex}/{pageSize}")
    @PreAuthorize("hasAuthority('read_user')")
    public ResponseEntity<Object> getUsers(
                                           @PathVariable(value = "sortType") int sortType,
                                           @PathVariable(value = "pageIndex") int pageIndex,
                                           @PathVariable(value = "pageSize") int pageSize,
                                           @RequestParam(required = false, defaultValue = "-1") int status,
                                           @RequestParam(required = false, defaultValue = "") String searchString){
        CommandResult result = userService.getAllUsers(searchString,status,sortType,pageIndex,pageSize);
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

    @PostMapping("/addRole")
    @PreAuthorize("hasAuthority('edit_role_user')")
    public ResponseEntity<Object> AddNewRole(Principal principal,@RequestBody Map<String, String> bodyRole){
        String username = bodyRole.get("username");
        int roleId = Integer.parseInt(bodyRole.get("roleId"));
        CommandResult result = userService.addRole(principal, username,roleId);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

//    @PostMapping("/removeRole")
//    @PreAuthorize("hasAuthority('edit_role_user')")
//    public ResponseEntity<Object> RemoveRole(Principal principal,@RequestBody Map<String, String> bodyRole){
//        String username = bodyRole.get("username");
//        int roleId = Integer.parseInt(bodyRole.get("roleId"));
//        CommandResult result = userService.RemoveRole(principal, username,roleId);
//        return new ResponseEntity<>(result.getData(), result.getStatus());
//    }

    @PostMapping("/banUser")
    @PreAuthorize("hasAuthority('edit_user')")
    public ResponseEntity<Object> banUser(@RequestBody Map<String, String> body){
        String username = body.get("username");
        CommandResult result = userService.banUser(username);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }

    @PostMapping("/activeUser")
    @PreAuthorize("hasAuthority('edit_user')")
    public ResponseEntity<Object> activeUser(@RequestBody Map<String, String> body){
        String username = body.get("username");
        CommandResult result = userService.activeUser(username);
        return new ResponseEntity<>(result.getData(), result.getStatus());
    }
}
