package com.example.diamkpo.Repositories;

import androidx.annotation.NonNull;

import com.example.diamkpo.Models.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class CategoryRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query categoryRef = firebaseFirestore
            .collection("Categories")
            .orderBy("priority", Query.Direction.ASCENDING);
    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    public CategoryRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCategoryData(){
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.categoryDataAdded(task.getResult().toObjects(CategoryModel.class));
                }else{
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }
}
