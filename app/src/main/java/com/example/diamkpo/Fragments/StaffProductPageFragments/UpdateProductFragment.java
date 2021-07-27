package com.example.diamkpo.Fragments.StaffProductPageFragments;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

public class UpdateProductFragment extends StaffProductPageFragment {

    @Override
    protected void inputCorrectData() {
        super.populateEditTexts();
    }

    @Override
    public void navigateToMenu(NavController navController) {
        navController.navigate((NavDirections) UpdateProductFragmentDirections.actionStaffUpdateProductPageFragmentToStaffMenuFragment());
    }

    @Override
    public void onClick(View v) {
        super.updateProductOnClick(v);
    }
}
