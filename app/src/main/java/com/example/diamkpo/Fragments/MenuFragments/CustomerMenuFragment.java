package com.example.diamkpo.Fragments.MenuFragments;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMenuFragment extends MenuFragment {

    @Override
    public void init(View view) {

    }

    @Override
    public void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController, List<MealModel> meals) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                CustomerMenuFragmentDirections.ActionMenuFragmentToProductPageFragment action = CustomerMenuFragmentDirections.actionMenuFragmentToProductPageFragment();
                action.setMealId(meals.get(position).getMealId());
                navController.navigate(action);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                reactToDoubleClick(meals.get(position).getMealId(), meals, position);
            }
        });
    }

    private void reactToDoubleClick(String mealId, List<MealModel> mealModelList, int position) {
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Liked")
                .document(currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists()) {
                                boolean liked = documentSnapshot.getBoolean("liked");
                                setNewLike(mealId, !liked, mealModelList, position);
                            }
                        }
                    }
                });
    }

    /**
     * reach to a high rated meal being liked
     */
    private void setNewLike(String mealId, boolean liked, List<MealModel> mealModelList, int position){
        HashMap<String, Object> likedMap = new HashMap<>();
        likedMap.put("liked", liked);
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Liked")
                .document(currentUserId)
                .set(likedMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isComplete()){
                            menuCategoryItemAdapter.notifyDataSetChanged();
                            if(liked) updateLikedMeals(mealId, mealModelList, position);
                            else deleteLikedMeal(mealId);
                        }
                    }
                });
    }

    private void deleteLikedMeal(String mealId) {
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Liked Meals")
                .document(mealId)
                .delete();
    }

    private void updateLikedMeals(String mealId, List<MealModel> mealModelList, int position) {
        Map<String, Object> likedMealMap = new HashMap<>();
        likedMealMap.put("mealId", mealModelList.get(position).getMealId());
        likedMealMap.put("name", mealModelList.get(position).getName());
        likedMealMap.put("image", mealModelList.get(position).getImage());

        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Liked Meals")
                .document(mealId)
                .set(likedMealMap);
    }
}
