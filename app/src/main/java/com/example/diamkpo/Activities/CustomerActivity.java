package com.example.diamkpo.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

//import com.example.diamkpo.Fragments.CustomerHomeFragmentDirections;
import com.example.diamkpo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class CustomerActivity extends AppCompatActivity {
    private BottomNavigationView customerBottomNavigationView;
    private NavController navController;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.customerFragmentContainer);
        navController = navHostFragment.getNavController();

        customerBottomNavigationView = findViewById(R.id.customerBottomNavigationView);

        //Perform item selected listener
        customerBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.customerHomeMenu:
                        navController.navigate(R.id.customerHomeFragment);
                        return true;
                    case R.id.customerBrowseMenu:
                        navController.navigate(R.id.searchFragment);
                        return true;
                    case R.id.customerOrderMenu:
                        navController.navigate(R.id.pastOrdersFragment);
                        return true;
                    case R.id.customerAccountMenu:
                        navController.navigate(R.id.accountFragment);
                        return true;
                }
                return false;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination,
                                             @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if(destination.getId() == R.id.customerHomeFragment
                || destination.getId() == R.id.searchFragment
                || destination.getId() == R.id.pastOrdersFragment
                || destination.getId() == R.id.accountFragment){
                    customerBottomNavigationView.setVisibility(View.VISIBLE);
                }
                else{
                    customerBottomNavigationView.setVisibility(View.GONE);
                }
            }
        });
    }
}