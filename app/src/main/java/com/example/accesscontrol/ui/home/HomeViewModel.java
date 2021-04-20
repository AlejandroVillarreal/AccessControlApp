package com.example.accesscontrol.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accesscontrol.data.network.services.LogoutResult;
import com.example.accesscontrol.domain.user.UsersManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel implements LogoutResult {

    private final UsersManager usersManager;
    private final MutableLiveData<Throwable> logoutError;
    private MutableLiveData<String> mText;

    @Inject
    public HomeViewModel(UsersManager usersManager) {
        this.usersManager = usersManager;
        logoutError = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void onSignOutSuccess() {

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = firebaseUser.getUid();
//        Log.d("Firebase User",uid);
//        mAuth.signOut();

    }

    @Override
    public void onSignOutFailure(Throwable error) {
        logoutError.postValue(error);
    }
}