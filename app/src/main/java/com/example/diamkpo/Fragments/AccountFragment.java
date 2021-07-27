package com.example.diamkpo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.Activities.SearchDeliveryAddressActivity;
import com.example.diamkpo.Adapters.AccountItemAdapter;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends Fragment {
    private GoogleSignInAccount account;

    private NavController navController;

    private ImageView accountImage;

    private TextView accountName;

    private RecyclerView accountRecycler;

    private AccountItemAdapter accountItemAdapter;

    private Map<String, Integer> accountItems;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        navController = Navigation.findNavController(view);

        accountItems = new HashMap<>();
        accountItems.put("Delivery Address", R.drawable.ic_baseline_location_on_24);
        accountItems.put("Liked Meals", R.drawable.green_heart_shape);
        accountItems.put("Sign Out", R.drawable.ic_baseline_time_to_leave_24);

        accountImage = view.findViewById(R.id.accountImage);
        Glide.with(view.getContext())
                .load(account.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(accountImage);

        accountName = view.findViewById(R.id.accountName);
        accountName.setText(account.getDisplayName());

        accountRecycler = view.findViewById(R.id.accountRecycler);
        accountItemAdapter = new AccountItemAdapter(getActivity());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        accountRecycler.setLayoutManager(linearLayoutManager1);
        accountItemAdapter.setAccountItems(accountItems);
        accountRecycler.setAdapter(accountItemAdapter);

        addClickSupportToRecycler(accountRecycler);

    }

    private void openSignOutDialog(){
        SignOutDialogFragment signOutDialogFragment = new SignOutDialogFragment();
        signOutDialogFragment.show(getParentFragmentManager(), signOutDialogFragment.getTag());
    }

    private void addClickSupportToRecycler(RecyclerView recycler){
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch(accountItems.keySet().toArray()[position].toString()){
                    case "Liked Meals":
                        navController.navigate(AccountFragmentDirections.actionAccountFragmentToLikedMealsFragment());
                        break;
                    case "Delivery Address":
                        Intent i = new Intent(getActivity(), SearchDeliveryAddressActivity.class);
                        startActivity(i);
                        break;
                    case "Sign Out":
                        openSignOutDialog();
                        break;
                }
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
            }
        });
    }
}