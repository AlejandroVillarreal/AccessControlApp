package com.example.accesscontrol.data.network.services;

public interface LogoutResult {
    void onSignOutSuccess();

    void onSignOutFailure(Throwable error);
}
