package com.example.accesscontrol.data.network.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.accesscontrol.data.mappers.UserMapper;
import com.example.accesscontrol.data.network.FirebaseTables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginService {

    private static final String TAG = "LoginService";
    private final FirebaseAuth mAuth;
    private final DatabaseReference database;
    private final UserMapper userMapper;

    @Inject
    public LoginService(UserMapper userMapper) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        this.userMapper = userMapper;
    }

    public void signIn(String email, String password, LoginResult loginResult) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignInWithEmail:success");
                            loginResult.onSignInSuccessful(mAuth.getCurrentUser().getUid());
                        } else {
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            loginResult.onGetUserFailure(task.getException());
                        }
                    }
                });
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        database.child(FirebaseTables.USERS.name).child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap<String, String> values = (HashMap<String, String>) task.getResult().getValue();
                    loginResult.onGetUserSuccessful(userMapper.map(values));
                    Log.d("LoginService", "r");
                } else {
                    loginResult.onGetUserFailure(task.getException());
                }
            }
        });
    }
}
