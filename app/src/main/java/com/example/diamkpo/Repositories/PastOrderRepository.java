package com.example.diamkpo.Repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.diamkpo.Models.PastOrderModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class PastOrderRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private GoogleSignInAccount account;
    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    public PastOrderRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getPastOrderData(Context context){
        account = GoogleSignIn.getLastSignedInAccount(context);
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .collection("Past Orders")
                .orderBy("orderNumber", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            onFirestoreTaskComplete.pastOrderDataAdded(task.getResult().toObjects(PastOrderModel.class));
                        }else{
                            onFirestoreTaskComplete.onError(task.getException());
                        }
                    }
                });
    }
}
