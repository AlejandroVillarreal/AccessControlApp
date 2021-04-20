package com.example.accesscontrol.domain.user;

import com.example.accesscontrol.data.entities.UserEntity;
import com.example.accesscontrol.data.network.services.LoginResult;
import com.example.accesscontrol.data.network.services.LogoutResult;
import com.example.accesscontrol.data.network.services.RegisterResult;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class UsersRepository {
    private final UsersRemoteSource usersRemoteSource;
    private final UsersLocalSource usersLocalSource;
    private final Executor executor;

    @Inject
    public UsersRepository(UsersRemoteSource usersRemoteSource, UsersLocalSource usersLocalSource, Executor executor) {
        this.usersRemoteSource = usersRemoteSource;
        this.usersLocalSource = usersLocalSource;
        this.executor = executor;
    }

    public void signIn(String email, String password, LoginResult loginResult) {
        executor.execute(() -> {
            usersRemoteSource.signIn(email, password, loginResult);
        });
    }

    public void signOut(LogoutResult logoutResult) {
        executor.execute(() -> {
            usersRemoteSource.signOut(logoutResult);
        });
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        executor.execute(() -> {
            UserEntity user = usersLocalSource.getUser(userId);
            if (user == null) {
                usersRemoteSource.getUserInfo(userId, loginResult);
            } else {
                loginResult.onGetUserSuccessful(user);
            }
        });
    }

    public void saveUser(UserEntity userEntity) {
        executor.execute(() -> {
            usersLocalSource.saveUser(userEntity);
        });
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        executor.execute(() -> {
            usersRemoteSource.createUser(userEntity, password, registerResult);
        });
    }

    public void saveUserId(String userId) {
        executor.execute(() -> {
            //usersRemoteSource.createUser(userEntity, password, registerResult);

        });
    }
}
