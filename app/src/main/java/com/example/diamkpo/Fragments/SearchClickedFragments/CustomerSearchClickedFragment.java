package com.example.diamkpo.Fragments.SearchClickedFragments;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.TouchListeners.ItemClickSupport;

import java.util.List;

public class CustomerSearchClickedFragment extends SearchClickedFragment {

    @Override
    public void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController,
                                          List<MealModel> filteredMeals, List<MealModel> allMeals) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                CustomerSearchClickedFragmentDirections.ActionSearchClickedFragmentToProductPageFragment action
                        = CustomerSearchClickedFragmentDirections.actionSearchClickedFragmentToProductPageFragment();
                if(filteredMeals.size() < 1) {
                    action.setMealId(allMeals.get(position).getMealId());
                }else{
                    action.setMealId(filteredMeals.get(position).getMealId());
                }
                navController.navigate((NavDirections) action);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                //nothing for now
            }
        });
    }

    @Override
    public void navigateToSearchFragment(NavController navController) {
        navController.navigate(CustomerSearchClickedFragmentDirections.actionSearchClickedFragmentToSearchFragment());
    }
}
