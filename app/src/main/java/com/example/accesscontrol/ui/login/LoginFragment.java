package com.example.accesscontrol.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.accesscontrol.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private NavController navController;
    private LoginViewModel loginViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private BottomNavigationView navView;
    public LoginFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.login_fragment, container, false);
        navController = NavHostFragment.findNavController(this);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextPassword = root.findViewById(R.id.editTextPassword);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navView = getActivity().findViewById(R.id.nav_view);
        //navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(false);
        Button loginButton = root.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this::login);
        Button registerButton = root.findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(this::register);

        //BottomNavigationView navView = root.findViewById(R.id.nav_view);
        //navView.findViewById(R.id.navigation_notifications).setVisibility(View.GONE);
        observe();
        return root;
    }

    public void observe() {
        loginViewModel.getEmailError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextEmail.setError("Field can't be empty");
            }
        });
        loginViewModel.getPasswordError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextPassword.setError("Field can't be empty");
            }
        });
        loginViewModel.getSignInError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        loginViewModel.getGetUserError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Unable to get user info",
                        Toast.LENGTH_SHORT).show();
            }
        });
        loginViewModel.getSignInSuccessful().observe(getViewLifecycleOwner(), success -> {
            if (success) {
//                NavHostFragment navHostFragment = (NavHostFragment) SupportFra

                navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationHome(loginViewModel.getUserId()));
                Log.d("user_id", loginViewModel.getUserId());
                setMenu(loginViewModel.getUserId());
//                findNavController()LoginFragmentDirections.actionLoginToHome();
            }
        });
    }

    //Button login
    public void login(View v) {
        String emailInput = editTextEmail.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();
        loginViewModel.connect(emailInput, passwordInput);


    }

    public void setUserPreferences() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
    }

    //Button Fragment
    public void register(View v) {
        navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationRegister());
    }

    private void setMenu(String user_id) {
        //final AtomicBoolean done = new AtomicBoolean(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("User id", user_id);
        final String[] user_type = new String[1];
        DatabaseReference databaseReference = database.getReference("Users").child(user_id).child("type");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_type[0] = snapshot.getValue().toString();
                Log.d("User Type ", user_type[0]);
                //done.set(true);
                switch (user_type[0]) {
                    case "residente":
                        navView.getMenu().findItem(R.id.navigation_notifications).setVisible(false);
                        navView.getMenu().findItem(R.id.historyFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.userManagementFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(true);
                        return;
                    case "admin":
                        navView.getMenu().findItem(R.id.navigation_notifications).setVisible(true);
                        navView.getMenu().findItem(R.id.historyFragment).setVisible(true);
                        navView.getMenu().findItem(R.id.userManagementFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(true);
                        return;
                    case "guardia":
                        navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(false);
                        navView.getMenu().findItem(R.id.userManagementFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.navigation_notifications).setVisible(true);
                        navView.getMenu().findItem(R.id.historyFragment).setVisible(true);
                        return;
                    case "":
                        navView.getMenu().findItem(R.id.navigation_notifications).setVisible(false);
                        navView.getMenu().findItem(R.id.historyFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.userManagementFragment).setVisible(false);
                        navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(true);
                        return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //while (user_type[0].isEmpty());
        //return user_type[0];
    }
}