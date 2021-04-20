package com.example.accesscontrol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.accesscontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private NavController navController;
    private String user_id;
    private HomeFragmentArgs homeFragmentArgs;

    private EditText editTextName;
    private EditText editTextEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
        //user_id = homeFragmentArgs.fromBundle(getArguments()).getActiveUser();
        //Log.d("Active user" ,user_id);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        navController = NavHostFragment.findNavController(this);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextName = root.findViewById(R.id.editTextName);
        logoutButton = root.findViewById(R.id.button2);
        logoutButton.setOnClickListener(this::logout);
        this.updateUser();
        return root;
    }

    public void logout(View v) {
        //homeViewModel.onSignOutSuccess();
        navController.navigate(HomeFragmentDirections.actionNavigationHomeToNavigationLogin());
    }

    public void updateUser() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, String> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot : task.getResult().getChildren()) {
                        String name = messageSnapshot.getKey();
                        String message = messageSnapshot.getValue().toString();
                        map.put(name, message);

                    }
                    editTextName.setText(map.get("name"));
                    editTextEmail.setText(map.get("email"));
                    //editTextPhone.setText(map.get("phone"));
                    //editTextBuisness.setText(map.get(""));
                }
            }
        });
    }
}