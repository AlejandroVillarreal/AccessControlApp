package com.example.accesscontrol.domain.user;

import com.example.accesscontrol.data.daos.UserDao;
import com.example.accesscontrol.data.entities.UserEntity;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class UsersLocalSource {
    private final UserDao userDao;

    @Inject
    public UsersLocalSource(UserDao userDao, Executor executor) {
        this.userDao = userDao;
    }

    public void saveUser(UserEntity userEntity) {
        userDao.insert(userEntity);
    }

    public UserEntity getUser(String userId) {
        return userDao.getUser(userId);
    }
}
