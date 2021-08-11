package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diamkpo.Adapters.CartItemsAdapter;
import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.UserModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.example.diamkpo.ViewModels.CartViewModel;
import com.example.diamkpo.ViewModels.MealViewModel;
import com.example.diamkpo.ViewModels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, CartItemsAdapter.CartItemClicked {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private RecyclerView cartItemsRecycler;

    private CartItemsAdapter cartItemsAdapter;

    private CartViewModel cartViewModel;

    private List<CartModel> cartItems;

    private TextView goToCheckoutTv;
    private TextView cartTotalPrice;

    private Double totalPrice;


    private static final String TAG = "CART_SHEET_HOME_LOG";

    public CartBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        totalPrice = 0.0;

        cartItems = new ArrayList<>();

        goToCheckoutTv = view.findViewById(R.id.goToCheckoutTv);
        cartTotalPrice = view.findViewById(R.id.cartTotalPrice);

        //cart MealModel recycler and adapter
        cartItemsRecycler = view.findViewById(R.id.cartItemsRecycler);
        cartItemsAdapter = new CartItemsAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        cartItemsRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.grey_line));
        cartItemsRecycler.addItemDecoration(dividerItemDecoration);
        cartItemsRecycler.setAdapter(cartItemsAdapter);

        getCartItems();

        addOnClickListeners();
    }

    private void getCartItems(){
        cartViewModel = new ViewModelProvider(this.requireActivity()).get(CartViewModel.class);
        cartViewModel.getCartData(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                cartItems = cartModels;
                cartItemsAdapter.setCartModels(cartModels);
                setCorrectTotalPrice(cartModels);
            }
        });
    }

    private void setCorrectTotalPrice(List<CartModel> cartModels) {
        totalPrice = 0.0;
        for(CartModel cm: cartModels){
            totalPrice = totalPrice + cm.getTotalPrice();
        }
        cartTotalPrice.setText("$" + round(totalPrice, 2));
    }

    private void addOnClickListeners() {
        goToCheckoutTv.setOnClickListener(this);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void openCart(){
        CheckoutBottomSheetFragment checkoutBottomSheetFragment = new CheckoutBottomSheetFragment(cartItems);
        checkoutBottomSheetFragment.show(getParentFragmentManager(), checkoutBottomSheetFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goToCheckoutTv:
                if(cartItems.size()>0)
                openCart();
                break;
        }
    }

    @Override
    public void onRemoveItemClicked(int position) {
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .collection("Cart")
                .document(cartItems.get(position).getCartId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            getCartItems();
                        }
                    }
                });
    }
}