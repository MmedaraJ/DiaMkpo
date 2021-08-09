package com.example.diamkpo.Fragments.SearchFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.Activities.SignInActivity;
import com.example.diamkpo.Adapters.SearchCategoryItemsAdapter;
import com.example.diamkpo.Fragments.CartBottomSheetFragment;
import com.example.diamkpo.ItemDecorations.GridSpacingItemDecoration;
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.example.diamkpo.ViewModels.CategoryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchFragment extends Fragment implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    private TextView searchFragmentHeaderTv;
    private TextView categoriesHeaderSearchFragment;
    private TextView staffSignOutTv;

    private TextView searchTextViewSearchFragment;
    private TextView addNewMealStaffSearch;

    private RecyclerView categoriesRecyclerSearchFragment;

    private SearchCategoryItemsAdapter searchCategoryItemsAdapter;

    private CategoryViewModel categoryViewModel;

    private List<CategoryModel> allCategories;

    private static final String TAG = "STAFF_FRAGMENT";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allCategories = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);

        addNewMealStaffSearch = view.findViewById(R.id.addNewMealStaffSearch);

        searchFragmentHeaderTv = view.findViewById(R.id.searchFragmentHeaderTv);
        categoriesHeaderSearchFragment = view.findViewById(R.id.categoriesHeaderSearchFragment);
        staffSignOutTv = view.findViewById(R.id.staffSignOutTv);

        searchTextViewSearchFragment = view.findViewById(R.id.searchTextViewSearchFragment);

        //recycler
        categoriesRecyclerSearchFragment = view.findViewById(R.id.categoriesRecyclerSearchFragment);
        searchCategoryItemsAdapter = new SearchCategoryItemsAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        int spanCount = 2;
        int spacing = 30;
        categoriesRecyclerSearchFragment.setLayoutManager(gridLayoutManager);
        categoriesRecyclerSearchFragment.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        categoriesRecyclerSearchFragment.setAdapter(searchCategoryItemsAdapter);

        //////////////////////////////////on Activity created///////////////////////////////////////////////
        categoryViewModel = new ViewModelProvider(this.requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getCategoryData().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModelList) {
                allCategories = categoryModelList;
                searchCategoryItemsAdapter.setCategoryModels(categoryModelList);
            }
        });
        addClickSupportToRecycler(categoriesRecyclerSearchFragment, navController);
        searchTextViewSearchFragment.setOnClickListener(this);
        init(view);
        setOnClickListeners();
    }

    public void initForCustomer(View view){
        addNewMealStaffSearch.setVisibility(View.GONE);
        staffSignOutTv.setVisibility(View.GONE);
    }

    public void initForStaff(View view){
        searchFragmentHeaderTv.setText(R.string.staff_search_header);
    }

    public abstract void setOnClickListeners();

    public void setStaffOnClickListeners(){
        addNewMealStaffSearch.setOnClickListener(this);
        staffSignOutTv.setOnClickListener(this);
    }

    public void setCustomerOnClickListeners(){

    }

    public abstract void init(View view);

    /**
     * add click support to recycler
     */
    public abstract void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController);

    public abstract void openSearchClickedFragment(NavController navController);

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.searchTextViewSearchFragment:
                openSearchClickedFragment(navController);
                break;
            case R.id.addNewMealStaffSearch:
                navController.navigate(StaffSearchFragmentDirections.actionStaffSearchFragmentToStaffAddNewProductPageFragment());
                break;
            case R.id.staffSignOutTv:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), SignInActivity.class);
                startActivity(i);
                break;
        }
    }
}