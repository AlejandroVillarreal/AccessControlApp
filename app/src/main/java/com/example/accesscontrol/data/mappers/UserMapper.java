package com.example.accesscontrol.data.mappers;

import com.example.accesscontrol.data.entities.UserEntity;

import java.util.HashMap;

import javax.inject.Inject;

public class UserMapper {

    @Inject
    public UserMapper() {

    }

    public UserEntity map(HashMap<String, String> values) {
        UserEntity user = new UserEntity();
        user.firebaseId = values.get("user_id");
        user.name = values.get("name");
        user.email = values.get("email");
        /*user.profile_pic = values.get("profile_pic");

         */
        return user;
    }
}
