package com.example.accesscontrol.di;

import android.content.Context;

import androidx.room.Room;

import com.example.accesscontrol.data.daos.UserDao;
import com.example.accesscontrol.data.db.AccesControlDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DatabaseModule {

    @Provides
    @Singleton
    public static Executor provideExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Provides
    @Singleton
    public static AccesControlDatabase provideDatabase(
            @ApplicationContext Context context,
            Executor roomExecutor
    ) {
        return Room.databaseBuilder(context, AccesControlDatabase.class, AccesControlDatabase.DATABASE_NAME)
                .setQueryExecutor(roomExecutor)
                .build();

    }

    //Por cada tabla se necesita un Dao
    @Provides
    public static UserDao provideUserDao(AccesControlDatabase database) {
        return database.userDao();
    }
}
