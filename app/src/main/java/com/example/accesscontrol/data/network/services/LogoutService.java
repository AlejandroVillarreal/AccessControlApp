package com.example.accesscontrol.data.network.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LogoutService {
    private static final String TAG = "LogoutService";
    private final FirebaseAuth mAuth;
    private final DatabaseReference database;

    @Inject
    public LogoutService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

    }

    public void signOut(LogoutResult logoutResult) {
        mAuth.signOut();
    }
}
