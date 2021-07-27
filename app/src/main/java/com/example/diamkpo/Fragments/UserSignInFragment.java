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
import android.widget.TextView;

import com.example.diamkpo.Activities.CustomerActivity;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserSignInFragment extends Fragment implements View.OnClickListener {
    private static final Integer RC_SIGN_IN = 0;
    private static final String TAG = "CHECK_SIGN_IN";

    private FirebaseFirestore firebaseFirestore;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private GoogleSignInClient mGoogleSignInClient;

    private SignInButton googleSignInButton;
    private TextView userSignInConfirmation;

    private boolean newUser;

    public UserSignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newUser = true;

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(this);

        userSignInConfirmation = view.findViewById(R.id.userSignInConfirmation);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(String.valueOf(R.string.webclient_id1))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    public GoogleSignInClient getGoogleSignInClient(){
        return mGoogleSignInClient;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null){
            //add text view with comment
            userSignInConfirmation.setText(R.string.unsuccessful_sign_in);
        }
        else{
            userSignInConfirmation.setText(R.string.successful_sign_in);
            checkIfThisIsUsersFirstSignUp(account);
        }

    }

    private void checkIfThisIsUsersFirstSignUp(GoogleSignInAccount account){
        firebaseFirestore
                .collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                if(id.equals(account.getId())){
                                    newUser = false;
                                    break;
                                }
                            }

                            if (newUser) createDatabase(account);
                            else openCustomerActivity();
                        }
                    }
                });
    }

    private void createDatabase(GoogleSignInAccount account){
        createUserMealInfo(account);
        createUserUsersInfo(account);
        openCustomerActivity();
    }

    private void openCustomerActivity(){
        Intent i = new Intent(getActivity(), CustomerActivity.class);
        startActivity(i);
    }

    public void createUserMealInfo(GoogleSignInAccount account){
        firebaseFirestore.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            HashMap<String, Object> likedMap = new HashMap<>();
                            likedMap.put("liked", false);

                            HashMap<String, Object> numOrders = new HashMap<>();
                            numOrders.put("numOrders", 0);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firebaseFirestore.collection("meals")
                                        .document(document.getId())
                                        .collection("Liked")
                                        .document(account.getId())
                                        .set(likedMap);

                                firebaseFirestore.collection("meals")
                                        .document(document.getId())
                                        .collection("Number of Orders")
                                        .document(account.getId())
                                        .set(numOrders);
                            }
                        }
                    }
                });
    }

    private void createUserUsersInfo(GoogleSignInAccount account){
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("deliveryAddress", "Set delivery address");
        userInfoMap.put("deliveryProvince", "");
        userInfoMap.put("totalNumberOfOrders", 0);
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .set(userInfoMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.googleSignInButton:
                signIn();
                break;
        }
    }
}