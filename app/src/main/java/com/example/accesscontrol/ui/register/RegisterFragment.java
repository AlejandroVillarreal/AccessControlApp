package com.example.accesscontrol.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.accesscontrol.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private NavController navController;
    private RegisterViewModel registerViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;

    public static RegisterFragment newInstance() {

        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_fragment, container, false);
        navController = NavHostFragment.findNavController(this);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        editTextName = root.findViewById(R.id.editTextName);
        Button registerButton = root.findViewById(R.id.buttonRegistrarse);
        registerButton.setOnClickListener(this::register);

        observe();

        return root;
    }

    public void observe() {
        registerViewModel.getEmailError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextEmail.setError("Field can't be empty");
            }
        });
        registerViewModel.getPasswordError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextPassword.setError("Field can't be empty");
            }
        });
        registerViewModel.getNameError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextName.setError("Field can't be empty");
            }
        });

        registerViewModel.getRegisterError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Registration failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        registerViewModel.getRegisterSuccessful().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                navController.navigate(RegisterFragmentDirections.actionNavigationRegisterToNavigationHome(registerViewModel.getUserId()));
            }
        });
    }

    public void register(View v) {
        String emailInput = editTextEmail.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();
        String nameInput = editTextName.getText().toString().trim();
        registerViewModel.register(emailInput, passwordInput, nameInput);

    }
}