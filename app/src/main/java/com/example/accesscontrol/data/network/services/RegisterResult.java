package com.example.accesscontrol.data.network.services;

import com.example.accesscontrol.data.entities.UserEntity;

public interface RegisterResult {
    void onRegisterSuccessful(UserEntity userEntity);

    void onRegisterFailure(Throwable error);
}
