package com.example.accesscontrol.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.accesscontrol.data.daos.UserDao;
import com.example.accesscontrol.data.entities.UserEntity;

@Database(
        entities = {UserEntity.class},
        version = 1
)
public abstract class AccesControlDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "AccessControl-app.db";

    public abstract UserDao userDao();

}
