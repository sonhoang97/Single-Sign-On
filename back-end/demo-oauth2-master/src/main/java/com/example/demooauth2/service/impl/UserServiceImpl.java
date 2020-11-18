package com.example.demooauth2.service.impl;

import com.example.demooauth2.modelView.users.UserProfileViewModel;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
            return new CommandResult(HttpStatus.CONFLICT, "Username has existed!");
        }
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(userDto.getPassword());
//        user.setEmail(userDto.getEmail());
        userRepository.save(userDto);
        return new CommandResult().Succeed();
    }

    @Override
    public CommandResult getProfile(Principal principal) {
        if(!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()){
            return new CommandResult(HttpStatus.UNAUTHORIZED,"Unauthenticated");
        }

        UserProfileViewModel user = userRepository.getProfileByUsername(principal.getName());
        if(!user.isValid()){
            return new CommandResult(HttpStatus.NOT_FOUND,"Not found user");
        }

        return new CommandResult().SucceedWithData(user);
    }
}
