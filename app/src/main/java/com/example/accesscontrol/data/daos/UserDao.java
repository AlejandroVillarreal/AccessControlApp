package com.example.accesscontrol.data.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.accesscontrol.data.entities.UserEntity;

@Dao
public abstract class UserDao extends EntityDao<UserEntity> {
    @Query("SELECT * FROM USER_ENTITY WHERE FIREBASE_ID = :userId")
    public abstract UserEntity getUser(String userId);
}
