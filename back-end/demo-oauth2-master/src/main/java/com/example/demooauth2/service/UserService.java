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

    CommandResult addRole(Principal principal,String username, int roleId);

    CommandResult RemoveRole(Principal principal,String username, int roleId);

    CommandResult getAllUsers(String searchString,int status,int sortType, int pageIndex, int pageSize);

    CommandResult banUser(String username);

    CommandResult activeUser(String username);
}
