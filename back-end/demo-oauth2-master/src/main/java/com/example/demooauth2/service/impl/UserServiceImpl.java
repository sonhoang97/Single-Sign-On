package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelView.pageList.PageList;
import com.example.demooauth2.modelView.users.UserProfileViewModel;
import com.example.demooauth2.repository.RefreshTokenRepository;
import com.example.demooauth2.repository.RoleRepository;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public CommandResult getAllUsers(String searchString, int status, int sortType, int pageIndex, int pageSize) {
        Sort sortable = Sort.by("username").ascending();
        if (sortType == 0) {
            sortable = Sort.by("username").ascending();
        }
        if (sortType == 1) {
            sortable = Sort.by("username").descending();
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sortable);
        List<UserProfileViewModel> users;
        int totalSize;
        if (status == -1) {
            users = userRepository.getAllUsersNonStatus(searchString, pageable);
            totalSize = userRepository.countSearchUsersNonStatus(searchString);


        } else if(status == 0 || status ==1 ) {
            if(status ==0){
            users = userRepository.getAllUsers(searchString, false, pageable);
            totalSize = userRepository.countSearchUsers(searchString,false);
            } else {
                users = userRepository.getAllUsers(searchString, true, pageable);
                totalSize = userRepository.countSearchUsers(searchString,true);
            }
        } else {
            return new CommandResult(HttpStatus.BAD_REQUEST, "Fail Status!");
        }
        PageList<UserProfileViewModel> result = new PageList<>(users, pageIndex, pageSize, totalSize);
        return new CommandResult().SucceedWithData(result);
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
                userRepository.updatePassword(userDto.getUsername(), userDto.getPassword());
                return new CommandResult().Succeed();
            }
        }
        Optional<RoleEntity> role = roleRepository.findByName("ROLE_user");
        if (role.isPresent()) {
            Set<RoleEntity> rolesDefault = new HashSet<>();
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
            refreshTokenRepository.deleteByUsername(principal.getName());
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
    public CommandResult addRole(Principal principal, String username, int roleId) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if (!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            Optional<UserEntity> userEntity = userRepository.findByUsername(username);
            if (!userEntity.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find user");
            }
            userEntity.get().AddNewRole(existRole.get());
            userRepository.save(userEntity.get());
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }

    @Override
    public CommandResult RemoveRole(Principal principal, String username, int roleId) {
        try {
            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated");
            }
            Optional<RoleEntity> existRole = roleRepository.findById(roleId);
            if (!existRole.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find role");
            }
            Optional<UserEntity> userEntity = userRepository.findByUsername(username);
            if (!userEntity.isPresent()) {
                return new CommandResult(HttpStatus.NOT_FOUND, "Can not find user: " + username);
            }
            userEntity.get().RemoveRole(existRole.get());
            userRepository.save(userEntity.get());
            return new CommandResult().Succeed();
        } catch (Exception ex) {
            return new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!");
        }
    }

    @Override
    public CommandResult banUser(String username){
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (!userEntity.isPresent()) {
            return new CommandResult(HttpStatus.NOT_FOUND, "Can not find user: " + username);
        }

        userEntity.get().setEnabled(false);
        userRepository.save(userEntity.get());
        return new CommandResult().Succeed();

    }

    @Override
    public CommandResult activeUser(String username){
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (!userEntity.isPresent()) {
            return new CommandResult(HttpStatus.NOT_FOUND, "Can not find user: " + username);
        }
        userEntity.get().setEnabled(true);
        userRepository.save(userEntity.get());
        return new CommandResult().Succeed();
    }


}
