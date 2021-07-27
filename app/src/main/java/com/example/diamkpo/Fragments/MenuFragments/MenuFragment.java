package com.example.diamkpo.Fragments.MenuFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.Adapters.MenuCategoriesAdapter;
import com.example.diamkpo.Adapters.MenuCategoryItemAdapter;
import com.example.diamkpo.Fragments.CartBottomSheetFragment;
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.R;
import com.example.diamkpo.ViewModels.CategoryViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuFragment extends Fragment implements View.OnClickListener, MenuCategoriesAdapter.MenuCategoryItemClicked {
    private ImageView backIconMenuFragment;
    private ImageView cartIconMenuFragment;

    private TextView menuHeader;

    private RecyclerView menuCategoriesRecycler;
    private RecyclerView menuCategoryItemRecycler;

    private MenuCategoriesAdapter menuCategoriesAdapter;
    protected MenuCategoryItemAdapter menuCategoryItemAdapter;

    private CategoryViewModel categoryViewModel;

    private NavController navController;

    private List<MealModel> meals;

    protected FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private static final String TAG = "MENU_FRAGMENT";
    protected String currentUserId;

    private int checkedPosition;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = "";
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            currentUserId = account.getId();
        }else {
        }

        checkedPosition = -1;
        checkedPosition = CustomerMenuFragmentArgs.fromBundle(getArguments()).getCheckedPosition();
        if(checkedPosition == -1){
            checkedPosition = StaffMenuFragmentArgs.fromBundle(getArguments()).getCheckedPosition();
        }

        meals = new ArrayList<>();

        backIconMenuFragment = view.findViewById(R.id.backIconMenuFragment);

        cartIconMenuFragment = view.findViewById(R.id.cartIconMenuFragment);

        menuHeader = view.findViewById(R.id.menuHeader);

        //menu category items recycler
        menuCategoriesRecycler = view.findViewById(R.id.menuCategoriesRecycler);
        menuCategoriesAdapter = new MenuCategoriesAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        menuCategoriesRecycler.setLayoutManager(linearLayoutManager);
        menuCategoriesRecycler.setAdapter(menuCategoriesAdapter);

        //menu category item items recycler
        menuCategoryItemRecycler = view.findViewById(R.id.menuCategoryItemRecycler);
        menuCategoryItemAdapter = new MenuCategoryItemAdapter(getActivity());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        menuCategoryItemRecycler.setLayoutManager(linearLayoutManager2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.green_line));
        menuCategoryItemRecycler.addItemDecoration(dividerItemDecoration);
        menuCategoryItemRecycler.setAdapter(menuCategoryItemAdapter);

        categoryViewModel = new ViewModelProvider(this.requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getCategoryData().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categories) {
                menuCategoriesAdapter.setCategories(categories);//if previous fragment was search frag
                if(checkedPosition > -1){
                    menuCategoriesAdapter.setSelected(checkedPosition);
                }
                onItemClicked(menuCategoriesAdapter.getCheckedPosition());
            }
        });

        navController = Navigation.findNavController(view);

        init(view);

        setOnClickListeners();
    }

    public abstract void init(View view);

    public void initForStaff(View view){
        cartIconMenuFragment.setVisibility(View.GONE);
    }

    private void setOnClickListeners() {
        cartIconMenuFragment.setOnClickListener(this);
        backIconMenuFragment.setOnClickListener(this);
    }

    private void getMealsThatMatchSelectedCategory() {
        String selectedCategory = menuCategoriesAdapter.getSelected().getName();
        firebaseFirestore.collection("meals")
                .whereArrayContains("category", selectedCategory)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            meals.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, "---------------------------------------- meal name is " + document.getData());
                                MealModel meal = document.toObject(MealModel.class);
                                Log.d(TAG, "---------------------------------------- meal name is " + meal.getName());
                                meals.add(meal);
                            }
                            setAdapter(meals);
                            addClickSupportToRecycler(menuCategoryItemRecycler, navController, meals);
                        }
                        else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setAdapter(List<MealModel> meals) {
        menuCategoryItemAdapter = new MenuCategoryItemAdapter(getActivity());
        menuCategoryItemAdapter.setMealModels(meals);
        menuCategoryItemRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuCategoryItemRecycler.setAdapter(menuCategoryItemAdapter);
        menuCategoriesRecycler.smoothScrollToPosition(menuCategoriesAdapter.getCheckedPosition());
    }

    private void openCart(){
        CartBottomSheetFragment cartBottomSheetFragment = new CartBottomSheetFragment();
        cartBottomSheetFragment.show(getParentFragmentManager(), cartBottomSheetFragment.getTag());
    }

    public abstract void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController, List<MealModel> meals);

    @Override
    public void onItemClicked(int position) {
        getMealsThatMatchSelectedCategory();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cartIconMenuFragment:
                openCart();
                break;
            case R.id.backIconMenuFragment:
                navController.popBackStack();
                break;
        }
    }
}