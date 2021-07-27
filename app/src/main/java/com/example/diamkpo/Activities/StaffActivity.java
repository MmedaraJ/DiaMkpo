package com.example.diamkpo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.diamkpo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class StaffActivity extends AppCompatActivity {
    private BottomNavigationView staffBottomNavigationView;
    private NavController navController;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.staffFragmentContainer);
        navController = navHostFragment.getNavController();

        staffBottomNavigationView = findViewById(R.id.staffBottomNavigationView);

        //Perform item selected listener
        staffBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.staffBrowseMenu:
                        navController.navigate(R.id.staffSearchFragment);
                        return true;
                    case R.id.staffMenuMenu:
                        navController.navigate(R.id.staffMenuFragment);
                        return true;
                }
                return false;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination,
                                             @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if(destination.getId() == R.id.staffSearchFragment
                        || destination.getId() == R.id.staffMenuFragment){
                    staffBottomNavigationView.setVisibility(View.VISIBLE);
                }
                else{
                    staffBottomNavigationView.setVisibility(View.GONE);
                }
            }
        });
    }
}