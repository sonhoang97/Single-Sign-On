package com.example.demooauth2.repository;

import com.example.demooauth2.modelEntity.UserEntity;
//import com.example.demooauth2.modelView.users.UserTokenViewModel;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.modelView.users.UserProfileViewModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer>  {

    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Query("delete from UserEntity u where u.username = :username")
    void deleteAllByUsername(String username);

    @Query("Select u.id from UserEntity u where u.username=:username")
    Integer findIdByUsername(String username);

    @Query("Select u.password from UserEntity u where u.username=:username")
    String findPasswordByUsername(String username);

    @Query("Select NEW com.example.demooauth2.modelView.users.UserTokenViewModel(u) from UserEntity u where u.username=:username")
    <UserTokenViewModel>
    UserTokenViewModel findUserByUsername(String username);

    @Query("Select NEW com.example.demooauth2.modelView.users.UserTokenViewModel(u) from UserEntity u where u.id=:userId")
    <UserTokenViewModel>
    UserTokenViewModel findUserByUserId(Integer userId);

    @Query("Select NEW com.example.demooauth2.modelView.users.UserProfileViewModel(u) from UserEntity u where u.username=:username")
    <UserProfileViewModel>
    UserProfileViewModel getProfileByUsername(String username);

    @Transactional
    @Modifying
    @Query("update UserEntity set password=:password where username=:username")
    void updatePassword(String username, String password);

    @Transactional
    @Modifying
    @Query("update UserEntity set firstname=:firstname, lastname = :lastname, email = :email, phonenumber = :phonenumber where username=:username")
    void updateProfile(String username,String firstname, String lastname, String email, String phonenumber);

    @Query("Select NEW com.example.demooauth2.modelView.users.UserProfileViewModel(u) from UserEntity u WHERE u.username LIKE %?1% OR u.email LIKE %?1%")
    List<UserProfileViewModel> getAllUsers(String searchString, Pageable pageable);
}
