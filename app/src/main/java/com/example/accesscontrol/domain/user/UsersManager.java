package com.example.accesscontrol.domain.user;

import com.example.accesscontrol.data.entities.UserEntity;
import com.example.accesscontrol.data.network.services.LoginResult;
import com.example.accesscontrol.data.network.services.LogoutResult;
import com.example.accesscontrol.data.network.services.RegisterResult;

import javax.inject.Inject;

public class UsersManager {
    private final UsersRepository usersRepository;


    @Inject
    public UsersManager(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean emailIsInvalid(String email) {
        return email.isEmpty();
    }

    public boolean passwordIsInvalid(String password) {
        return password.isEmpty();
    }

    public boolean nameIsInvalid(String name) {
        return name.isEmpty() || name.length() > 50;
//        if(name.isEmpty()){
//            return false;
//        } else if (name.length() > 50){
//            return false;
//        } else {
//            return true;
//        }
    }


    public void signIn(String email, String password, LoginResult loginResult) {

        usersRepository.signIn(email, password, loginResult);

    }

    public void signOut(LogoutResult logoutResult) {
        usersRepository.signOut(logoutResult);
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        usersRepository.getUserInfo(userId, loginResult);
    }

    public void saveUser(UserEntity userEntity) {
        usersRepository.saveUser(userEntity);
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        usersRepository.createUser(userEntity, password, registerResult);
    }

    public void saveUserId(String userId) {

    }

}
