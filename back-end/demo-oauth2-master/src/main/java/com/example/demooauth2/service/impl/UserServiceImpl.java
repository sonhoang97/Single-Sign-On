package com.example.demooauth2.service.impl;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.model.User;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    @Override
    public CommandResult registerNewUserAccount(User userDto) {
    if(findByUsername(userDto.getUsername())!=null){
        return new CommandResult(HttpStatus.CONFLICT, "Username has existed!");
    }
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(userDto.getPassword());
//        user.setEmail(userDto.getEmail());
        userRepository.save(userDto);
        return new CommandResult().Succeed();
    }
}
