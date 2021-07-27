package com.example.diamkpo.Fragments.SearchFragments;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.TouchListeners.ItemClickSupport;

public class StaffSearchFragment extends SearchFragment {

    @Override
    public void init(View view) {
        super.initForStaff(view);
    }

    @Override
    public void setOnClickListeners() {
        super.setStaffOnClickListeners();
    }

    @Override
    public void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                StaffSearchFragmentDirections.ActionStaffSearchFragmentToMenuFragment2 action = StaffSearchFragmentDirections.actionStaffSearchFragmentToMenuFragment2();
                action.setCheckedPosition(position);
                navController.navigate((NavDirections) action);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                //nothing for now
            }
        });
    }

    @Override
    public void openSearchClickedFragment(NavController navController) {
        navController.navigate(StaffSearchFragmentDirections.actionStaffSearchFragmentToSearchClickedFragment2());
    }
}
