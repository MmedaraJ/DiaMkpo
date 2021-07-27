package com.example.diamkpo.Fragments.SearchFragments;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.TouchListeners.ItemClickSupport;

public class CustomerSearchFragment extends SearchFragment {

    @Override
    public void init(View view) {
        super.initForCustomer(view);
    }

    @Override
    public void setOnClickListeners() {
        super.setCustomerOnClickListeners();
    }


    @Override
    public void addClickSupportToRecycler(RecyclerView recyclerView, NavController navController) {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                CustomerSearchFragmentDirections.ActionSearchFragmentToMenuFragment action = CustomerSearchFragmentDirections.actionSearchFragmentToMenuFragment();
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
        navController.navigate(CustomerSearchFragmentDirections.actionSearchFragmentToSearchClickedFragment());
    }
}
