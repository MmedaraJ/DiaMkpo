package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.Adapters.LikedMealsAdapter;
import com.example.diamkpo.Adapters.YourFavouritesAdapter;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikedMealsFragment extends Fragment implements View.OnClickListener {
    private RecyclerView likedMealsRecycler;

    private LikedMealsAdapter likedMealsAdapter;

    private FirebaseFirestore firebaseFirestore;

    private NavController navController;

    private GoogleSignInAccount account;

    private ImageView backIconLikedMealsFragment;

    private String currentUserId;

    private List<String> mealIds;

    public LikedMealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liked_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = "";

        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            currentUserId = account.getId();
        }

        backIconLikedMealsFragment = view.findViewById(R.id.backIconLikedMealsFragment);

        navController = Navigation.findNavController(view);

        likedMealsRecycler = view.findViewById(R.id.likedMealsRecycler);
        likedMealsAdapter = new LikedMealsAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        likedMealsRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.green_line));
        likedMealsRecycler.addItemDecoration(dividerItemDecoration);
        likedMealsRecycler.setAdapter(likedMealsAdapter);

        mealIds = new ArrayList<>();

        getLikedMeals();

        setOnClickListener();
    }

    public void setOnClickListener(){
        backIconLikedMealsFragment.setOnClickListener(this);
    }

    private void getLikedMeals() {
        Map<String, Object> likeMealsMap = new HashMap<>();
        mealIds = new ArrayList<>();
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Liked Meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                likeMealsMap.put(document.getString("name"), document.getString("image"));
                                mealIds.add(document.getString("mealId"));
                            }
                            addClickSupportToRecycler(likedMealsRecycler, mealIds);
                            likedMealsAdapter.setLikedMealsMap(likeMealsMap);
                        }
                    }
                });
    }

    private void addClickSupportToRecycler(RecyclerView recycler, List<String> allMealIds){
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                navigateToProductPage(allMealIds.get(position));
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
            }
        });
    }

    private void navigateToProductPage(String mealId){
        LikedMealsFragmentDirections.ActionLikedMealsFragmentToProductPageFragment action = LikedMealsFragmentDirections.actionLikedMealsFragmentToProductPageFragment();
        action.setMealId(mealId);
        navController.navigate(action);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backIconLikedMealsFragment:
                navController.popBackStack();
                break;
        }
    }
}