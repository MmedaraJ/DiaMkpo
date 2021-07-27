package com.example.diamkpo.Fragments.StaffProductPageFragments;

import android.view.View;

import androidx.navigation.NavController;

public class AddNewProductFragment extends StaffProductPageFragment {
    @Override
    protected void inputCorrectData() {
        super.emptyEditTexts();
    }

    @Override
    public void navigateToMenu(NavController navController) {
        navController.navigate(AddNewProductFragmentDirections.actionStaffAddNewProductPageFragmentToStaffSearchFragment());
    }

    @Override
    public void onClick(View v) {
        super.addProductOnClick(v);
    }
}
