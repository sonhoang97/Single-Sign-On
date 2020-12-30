package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelView.users.UserProfileViewModel;
import com.example.demooauth2.repository.RoleRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    @Override
    public CommandResult registerNewUserAccount(UserEntity userDto) {
        if (findByUsername(userDto.getUsername()) != null) {
            if (!userDto.isLoggedInFb()) {
                return new CommandResult(HttpStatus.CONFLICT, "Username has existed!");
            } else {
                userRepository.deleteAllByUsername(userDto.getUsername());
            }
        }
        Optional<RoleEntity> role = roleRepository.findByName("ROLE_operator");
        if(role.isPresent()){
            List<RoleEntity> rolesDefault = new ArrayList<>();
            rolesDefault.add(role.get());
            userDto.setRoles(rolesDefault);
        }
        userRepository.save(userDto);
        return new CommandResult().Succeed();
    }

    @Override
    public CommandResult getProfile(Principal principal) {
        if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
            return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

        UserProfileViewModel user = userRepository.getProfileByUsername(principal.getName());
        if (!user.isValid()) {
            return new CommandResult(HttpStatus.NOT_FOUND, "Not found user");
        }

        return new CommandResult().SucceedWithData(user);
    }

    @Override
    public CommandResult changePassword(Principal principal, Map<String, String> bodyPassword) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }

            String currentPassword = bodyPassword.get("currentPassword");
            String newPassword = bodyPassword.get("newPassword");
            String confirmPassword = bodyPassword.get("confirmPassword");
            if (currentPassword == null || currentPassword.isEmpty() || newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Empty password!");
            }
            if (!newPassword.equals(confirmPassword)) {
                return new CommandResult(HttpStatus.CONFLICT, "Wrong confirm password!");
            }

            String storePassword = userRepository.findPasswordByUsername(principal.getName());
            if (storePassword == null || storePassword.isEmpty()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Not found current password!");
            }
            boolean passwordValid = passwordEncoder.matches(currentPassword, storePassword);
            if (!passwordValid) {
                return new CommandResult(HttpStatus.CONFLICT, "Wrong current password!");
            }

            String storeNewPassword = passwordEncoder.encode(newPassword);
            userRepository.updatePassword(principal.getName(), storeNewPassword);
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }

    @Override
    public CommandResult updateProfile(Principal principal, Map<String, String> bodyProfile) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }

            String firstname = bodyProfile.get("firstname");
            String lastname = bodyProfile.get("lastname");
            String email = bodyProfile.get("email");
            String phonenumber = bodyProfile.get("phonenumber");

            if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty() || email == null || email.isEmpty() || phonenumber == null || phonenumber.isEmpty()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Something are empties!");
            }
            userRepository.updateProfile(principal.getName(), firstname, lastname, email, phonenumber);
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }

    @Override
    public CommandResult addRole(Principal principal, int roleId) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            UserEntity userEntity =  userRepository.findByUsername(principal.getName()).get();
            userEntity.AddNewRole(existRole.get());
            userRepository.save(userEntity);
            return  new CommandResult().Succeed();
        }
        catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }
    @Override
    public CommandResult RemoveRole(Principal principal, int roleId) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if(!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            UserEntity userEntity =  userRepository.findByUsername(principal.getName()).get();
            userEntity.RemoveRole(existRole.get());
            userRepository.save(userEntity);
            return  new CommandResult().Succeed();
        }
        catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }
}
