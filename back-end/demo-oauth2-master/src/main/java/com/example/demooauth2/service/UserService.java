package com.example.demooauth2.service;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findByUsername(String username);

    CommandResult registerNewUserAccount(UserEntity user);
}
