package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findByUsername(String username);

    CommandResult registerNewUserAccount(UserEntity user);

    CommandResult getProfile(Principal principal);

    CommandResult changePassword(Principal principal, Map<String, String> bodyPassword);

    CommandResult updateProfile(Principal principal, Map<String, String> bodyProfile);

}
