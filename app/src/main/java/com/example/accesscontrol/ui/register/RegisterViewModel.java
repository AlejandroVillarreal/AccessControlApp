package com.example.accesscontrol.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accesscontrol.data.entities.UserEntity;
import com.example.accesscontrol.data.network.services.RegisterResult;
import com.example.accesscontrol.domain.user.UsersManager;
import com.example.accesscontrol.domain.user.exceptions.EmailValidationException;
import com.example.accesscontrol.domain.user.exceptions.NameValidationException;
import com.example.accesscontrol.domain.user.exceptions.PasswordValidationException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel implements RegisterResult {
    private final UsersManager usersManager;
    private final MutableLiveData<Throwable> emailError;
    private final MutableLiveData<Throwable> passwordError;
    private final MutableLiveData<Throwable> nameError;
    private final MutableLiveData<Throwable> registerError;
    private final MutableLiveData<Boolean> registerSuccessful;
    private MutableLiveData<Throwable> phoneError;
    private String userId;

    @Inject
    public RegisterViewModel(UsersManager usersManager) {
        this.usersManager = usersManager;
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        nameError = new MutableLiveData<>();
        registerError = new MutableLiveData<>();
        registerSuccessful = new MutableLiveData<>();
    }

    public void register(String email, String password, String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.name = name;

        boolean hasError = false;

        if (usersManager.emailIsInvalid(userEntity.email)) {
            emailError.postValue(new EmailValidationException());
            hasError = true;
        }
        if (usersManager.passwordIsInvalid(password)) {
            passwordError.postValue(new PasswordValidationException());
            hasError = true;
        }
        if (usersManager.nameIsInvalid(userEntity.name)) {
            nameError.postValue(new NameValidationException());
            hasError = true;
        }
        if (hasError) {
            return;
        }
        usersManager.createUser(userEntity, password, this);
    }

    @Override
    public void onRegisterSuccessful(UserEntity userEntity) {
        usersManager.saveUser(userEntity);
        userId = userEntity.firebaseId;
        registerSuccessful.postValue(true);
    }

    @Override
    public void onRegisterFailure(Throwable error) {
        registerSuccessful.postValue(true);

    }

    // TODO: Implement the ViewModel
    public LiveData<Throwable> getEmailError() {
        return emailError;
    }

    public LiveData<Throwable> getPasswordError() {
        return passwordError;
    }

    public LiveData<Throwable> getNameError() {
        return nameError;
    }

    public LiveData<Throwable> getRegisterError() {
        return registerError;
    }

    public LiveData<Boolean> getRegisterSuccessful() {
        return registerSuccessful;
    }

    public String getUserId() {
        return userId;
    }
}