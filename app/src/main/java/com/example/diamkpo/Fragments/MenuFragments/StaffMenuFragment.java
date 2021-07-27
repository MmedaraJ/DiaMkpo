package com.example.diamkpo.Fragments.MenuFragments;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.TouchListeners.ItemClickSupport;

import java.util.List;

public class StaffMenuFragment extends MenuFragment {

    @Override
    public void init(View view) {
        super.initForStaff(view);
    }

    @Override
    public void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController, List<MealModel> meals) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                StaffMenuFragmentDirections.ActionMenuFragment2ToStaffUpdateProductPageFragment action = StaffMenuFragmentDirections.actionMenuFragment2ToStaffUpdateProductPageFragment();
                action.setMealId(meals.get(position).getMealId());
                navController.navigate((NavDirections) action);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
            }
        });
    }
}
