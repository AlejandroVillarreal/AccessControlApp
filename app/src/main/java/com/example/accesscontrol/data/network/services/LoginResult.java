package com.example.accesscontrol.data.network.services;

import com.example.accesscontrol.data.entities.UserEntity;

public interface LoginResult {

    void onSignInSuccessful(String userId);

    void onSignInFailure(Throwable error);

    void onGetUserSuccessful(UserEntity userEntity);

    void onGetUserFailure(Throwable error);
}
