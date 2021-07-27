package com.example.diamkpo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    private ProgressBar startProgress;
    private TextView startFeedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startProgress = findViewById(R.id.startProgressBar);
        startFeedbackText = findViewById(R.id.initializingTv);
        startFeedbackText.setText(R.string.checking_user_account);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //after 2000 milli secs of this activity running, check current user
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
                //after main activity has opened, close this activity.
                //Do not leave it running in the background
                finish();
            }
        }, 2000);
    }

    private void checkUser(){
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null){
            startFeedbackText.setText(R.string.you_have_to_sign_in);
            //new intent to sign in activity
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
        }
        else{
            startFeedbackText.setText(R.string.successful_sign_in);
            //new intent to customer activity
            Intent i = new Intent(MainActivity.this, CustomerActivity.class);
            startActivity(i);
        }
    }
}
