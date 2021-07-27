package com.example.diamkpo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diamkpo.Activities.CustomerActivity;
import com.example.diamkpo.Activities.MainActivity;
import com.example.diamkpo.Activities.SignInActivity;
import com.example.diamkpo.Activities.StaffActivity;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class StaffSignInFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText staffEmailEditText;
    private EditText staffPasswordEditText;

    private TextView staffSignInConfirmation;

    private Button staffSignInButton;

    public StaffSignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        staffEmailEditText = view.findViewById(R.id.staffEmailEditText);
        staffPasswordEditText = view.findViewById(R.id.staffPasswordEditText);

        staffSignInConfirmation = view.findViewById(R.id.staffSignInConfirmation);

        staffSignInButton = view.findViewById(R.id.staffSignInButton);
        staffSignInButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getActivity(), StaffActivity.class);
            startActivity(i);

        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent i;
        if(currentUser == null){
            //add text view with comment
            staffSignInConfirmation.setText(R.string.unsuccessful_sign_in);
        }
        else{
            //new intent to customer activity (successful sign in)
            staffSignInConfirmation.setText(R.string.successful_sign_in);

            i = new Intent(getActivity(), StaffActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.staffSignInButton:
                signIn(staffEmailEditText.getText().toString(), staffPasswordEditText.getText().toString());
                break;
        }
    }
}