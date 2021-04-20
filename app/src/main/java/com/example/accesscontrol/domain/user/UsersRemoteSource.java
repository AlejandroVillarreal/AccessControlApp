package com.example.accesscontrol.domain.user;

import com.example.accesscontrol.data.entities.UserEntity;
import com.example.accesscontrol.data.network.services.LoginResult;
import com.example.accesscontrol.data.network.services.LoginService;
import com.example.accesscontrol.data.network.services.LogoutResult;
import com.example.accesscontrol.data.network.services.LogoutService;
import com.example.accesscontrol.data.network.services.RegisterResult;
import com.example.accesscontrol.data.network.services.RegisterService;

import javax.inject.Inject;

public class UsersRemoteSource {
    private final LoginService loginService;
    private final RegisterService registerService;
    private LogoutService logoutService;

    @Inject
    public UsersRemoteSource(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    public void signIn(String email, String password, LoginResult loginResult) {

        loginService.signIn(email, password, loginResult);
    }

    public void signOut(LogoutResult logoutResult) {
        logoutService.signOut(logoutResult);
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        loginService.getUserInfo(userId, loginResult);
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        registerService.createUser(userEntity, password, registerResult);
    }
}
