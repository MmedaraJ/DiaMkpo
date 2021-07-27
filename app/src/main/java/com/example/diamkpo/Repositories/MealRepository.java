package com.example.diamkpo.Repositories;

import androidx.annotation.NonNull;

import com.example.diamkpo.Models.MealModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class MealRepository {
    private static final int LIMIT = 4;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Query mealRef = firebaseFirestore.collection("meals");
    private Query highestRatedMealsRef = firebaseFirestore
            .collection("meals")
            .orderBy("avgRating", Query.Direction.DESCENDING)
            .limit(10);

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    public MealRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getMealData() {
        mealRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.mealDataAdded(task.getResult().toObjects(MealModel.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public void getHighestRatedMeals(){
        highestRatedMealsRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            onFirestoreTaskComplete.highestRatedMealsDataAdded(task.getResult().toObjects(MealModel.class));
                        }
                        else{
                            onFirestoreTaskComplete.onError(task.getException());
                        }
                    }
                });
    }

    public void getBestMealsData(String category) {
        firebaseFirestore
                .collection("meals")
                .whereArrayContains("category", category)
                //.limit(LIMIT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            onFirestoreTaskComplete.bestMealsDataAdded(task.getResult().toObjects(MealModel.class));
                        } else {
                            onFirestoreTaskComplete.onError(task.getException());
                        }
                    }
        });
    }
}
