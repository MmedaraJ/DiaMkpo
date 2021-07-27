package com.example.diamkpo.Repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.diamkpo.Models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class UserRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private OnFirestoreTaskComplete onFirestoreTaskComplete;
    private GoogleSignInAccount account;

    public UserRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getUserData(Context context){
        account = GoogleSignIn.getLastSignedInAccount(context);
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            onFirestoreTaskComplete.userDataAdded(task.getResult().toObject(UserModel.class));
                        }else{

                        }
                    }
                });
    }
}
