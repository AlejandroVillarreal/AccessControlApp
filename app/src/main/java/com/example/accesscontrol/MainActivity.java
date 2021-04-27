package com.example.accesscontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Log.d("Shared Preferences", sharedPreferences.toString());
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //Log.d("Current user",user.getUid());
        if (user == null) {
            return;
        } else {
            setMenu(user);
        }


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_login || destination.getId() == R.id.navigation_register) {
                    navView.setVisibility(View.INVISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } else {
                    navView.setVisibility(View.VISIBLE);
                    //navView.getMenu().findItem(R.id.navigation_notifications).setVisible(false);
                }
            }
        });


    }

    private String setMenu(FirebaseUser user) {
        //final AtomicBoolean done = new AtomicBoolean(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String user_id = user.getUid();
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

                    case "admin":
                        return;
                    case "guardia":
                        navView.getMenu().findItem(R.id.navigation_dashboard).setVisible(false);
                        navView.getMenu().findItem(R.id.userManagementFragment).setVisible(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //while (user_type[0].isEmpty());
        return user_type[0];
    }

}