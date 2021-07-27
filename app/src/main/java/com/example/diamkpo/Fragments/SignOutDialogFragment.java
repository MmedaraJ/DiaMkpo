package com.example.diamkpo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.diamkpo.Activities.SignInActivity;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class SignOutDialogFragment extends DialogFragment implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;

    private TextView confirmSignOutTv;

    private Button noButton;
    private Button yesButton;

    public SignOutDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_out_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(String.valueOf(R.string.webclient_id1))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        confirmSignOutTv = view.findViewById(R.id.confirmSignOutTv);
        noButton = view.findViewById(R.id.noButton);
        yesButton = view.findViewById(R.id.yesButton);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
    }

    private void signOut() {
        mGoogleSignInClient
                .signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        revokeAccess();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient
                .revokeAccess()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        dismiss();
                        Intent i = new Intent(getActivity(), SignInActivity.class);
                        startActivity(i);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.noButton:
                dismiss();
                break;
            case R.id.yesButton:
                signOut();
                break;
        }
    }
}