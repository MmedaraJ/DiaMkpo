package com.example.diamkpo.Fragments.SearchClickedFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.diamkpo.Adapters.SearchResultItemAdapter;
import com.example.diamkpo.ItemDecorations.GridSpacingItemDecoration;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.R;
import com.example.diamkpo.ViewModels.MealViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchClickedFragment extends Fragment implements View.OnClickListener {
    private NavController navController;

    private MealViewModel mealViewModel;

    private ImageView closeButtonSearchClickedFragment;

    private SearchView searchViewSearchClickedFragment;

    private TextView resultsTvSearchClicked;

    private RecyclerView searchResultsRecycler;

    private SearchResultItemAdapter searchResultItemAdapter;

    private List<MealModel> allMeals;
    private List<MealModel> filteredMeals;

    public SearchClickedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_clicked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filteredMeals = new ArrayList<>();
        allMeals = new ArrayList<>();

        navController = Navigation.findNavController(view);

        closeButtonSearchClickedFragment = view.findViewById(R.id.closeButtonSearchClickedFragment);
        searchViewSearchClickedFragment = view.findViewById(R.id.searchViewSearchClickedFragment);
        searchViewSearchClickedFragment.setQuery("", false);
        resultsTvSearchClicked = view.findViewById(R.id.resultsTvSearchClicked);

        //highest rated MealModel recycler and adapter
        searchResultsRecycler = view.findViewById(R.id.searchResultsRecycler);
        searchResultItemAdapter = new SearchResultItemAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        int spanCount = 3;
        int spacing = 30;
        searchResultsRecycler.setLayoutManager(gridLayoutManager);
        searchResultsRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        searchResultsRecycler.setHasFixedSize(true);
        searchResultsRecycler.setAdapter(searchResultItemAdapter);

        //////////////////////////////////on Activity created///////////////////////////////////////////////
        mealViewModel = new ViewModelProvider(this.requireActivity()).get(MealViewModel.class);
        mealViewModel.getMealsModelData().observe(getViewLifecycleOwner(), new Observer<List<MealModel>>() {
            @Override
            public void onChanged(List<MealModel> mealModelList) {
                allMeals = new ArrayList<>(mealModelList);
                searchResultItemAdapter.setMealModels(mealModelList);
                addClickSupportToRecycler(searchResultsRecycler, navController, filteredMeals, mealModelList);
            }
        });

        searchViewSearchClickedFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mealFilter(newText);
                return false;
            }
        });
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        closeButtonSearchClickedFragment.setOnClickListener(this);
    }

    private void mealFilter(String newText) {
        filteredMeals = new ArrayList<>();
        for(MealModel meal: allMeals){
            if(meal.getName().toLowerCase().contains(newText)
                    || meal.getDescription().toLowerCase().contains(newText)
                    || meal.getCategory().contains(newText)
                    || meal.getCategory().contains(newText.toUpperCase())
                    || String.valueOf(meal.getAvgRating()).contains(newText)
                    || String.valueOf(meal.getDealPrice()).contains(newText)
                    || String.valueOf(meal.getNormalPrice()).contains(newText)){
                filteredMeals.add(meal);
            }
        }
        addClickSupportToRecycler(searchResultsRecycler, navController, filteredMeals, allMeals);
        searchResultItemAdapter.setMealModels(filteredMeals);
    }

    /**
     * add click support to recycler
     */
    public abstract void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController,
                                                   List<MealModel> filteredMeals, List<MealModel> allMeals);

    public abstract void navigateToSearchFragment(NavController navController);

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.closeButtonSearchClickedFragment:
                navigateToSearchFragment(navController);
                break;
        }
    }
}