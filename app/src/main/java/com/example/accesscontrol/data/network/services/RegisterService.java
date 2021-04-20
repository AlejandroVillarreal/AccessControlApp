package com.example.accesscontrol.data.network.services;

import androidx.annotation.NonNull;

import com.example.accesscontrol.data.entities.UserEntity;
import com.example.accesscontrol.data.mappers.UserMapper;
import com.example.accesscontrol.data.network.FirebaseTables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import javax.inject.Inject;

public class RegisterService {

    private static final String TAG = "RegisterService";
    private final DatabaseReference database;
    private final UserMapper userMapper;
    private FirebaseAuth mAuth;

    @Inject
    public RegisterService(UserMapper userMapper) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        this.userMapper = userMapper;
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(userEntity.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userEntity.firebaseId = mAuth.getCurrentUser().getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", userEntity.email);
                    hashMap.put("user_id", userEntity.firebaseId);
                    hashMap.put("name", userEntity.name);

                    database.child(FirebaseTables.USERS.name).child(userEntity.firebaseId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                registerResult.onRegisterSuccessful(userEntity);
                            } else {
                                registerResult.onRegisterFailure(task.getException());
                            }
                        }
                    });
                } else {
                    registerResult.onRegisterFailure(task.getException());
                }
            }
        });
    }
}
