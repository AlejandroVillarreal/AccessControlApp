package com.example.accesscontrol.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.accesscontrol.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SharedPreferencesManager {

    static final String USER_ID_KEY = "USER_ID";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesManager(@ApplicationContext Context context) {

        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void saveUserId(String userId) {
        sharedPreferences.edit().putString(USER_ID_KEY, userId).commit();

    }
}
