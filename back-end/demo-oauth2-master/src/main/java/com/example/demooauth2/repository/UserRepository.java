package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.UserEntity;
//import com.example.demooauth2.modelView.users.UserTokenViewModel;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);
    
    @Query("Select NEW com.example.demooauth2.modelView.users.UserTokenViewModel(u) from UserEntity u where u.username=:username")
    <UserTokenViewModel>
    UserTokenViewModel findUserByUsername(String username);

    @Query("Select NEW com.example.demooauth2.modelView.users.UserTokenViewModel(u) from UserEntity u where u.id=:userId")
    <UserTokenViewModel>
    UserTokenViewModel findUserByUserId(Integer userId);
}
