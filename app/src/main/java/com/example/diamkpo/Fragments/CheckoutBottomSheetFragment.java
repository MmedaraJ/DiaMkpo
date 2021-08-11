package com.example.diamkpo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diamkpo.Activities.SearchDeliveryAddressActivity;
import com.example.diamkpo.Adapters.CartItemsAdapter;
import com.example.diamkpo.Adapters.CheckoutItemsAdapter;
import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.Models.UserModel;
import com.example.diamkpo.R;
import com.example.diamkpo.ViewModels.CartViewModel;
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

public class CheckoutBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, CartItemsAdapter.CartItemClicked/*CheckoutItemsAdapter.CheckoutItemClicked*/ {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private NavHostFragment navHostFragment;

    private NavController navController;

    private CartViewModel cartViewModel;

    private LinearLayout payNowLinearLayout;

    private ConstraintLayout changeDeliveryAddressLayout;

    private ImageView cancelCheckout;

    private TextView addItemsCheckout;
    private TextView subtotalPriceCheckout;
    private TextView deliveryFeePriceCheckout;
    private TextView taxesPriceCheckout;
    private TextView totalPricePriceCheckout;
    private TextView totalCheckoutPrice;
    private TextView deliveryAddressTv;
    private TextView deliveryProvinceTv;

    private RecyclerView checkoutItemsRecycler;

    private UserViewModel userViewModel;
    private UserModel thisUserModel;

    private CartItemsAdapter cartItemsAdapter;

    private Double subTotalPrice;
    private Double deliveryFee;
    private Double taxes;
    private Double totalPrice;

    private String deliveryAddress;
    private String deliveryProvince;

    private List<CartModel> cartItems;

    private String pastOrderId;
    private String image;

    private int orderNumber;

    private static final String TAG = "CHECKOUT";

    public CheckoutBottomSheetFragment() {
        // Required empty public constructor
    }

    public CheckoutBottomSheetFragment(List<CartModel> cartModels) {
        cartItems = cartModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        subTotalPrice = 0.00;
        deliveryFee = 0.00;
        taxes = 0.00;
        totalPrice = 0.00;

        orderNumber = 0;

        deliveryAddress = "";
        deliveryProvince = "";
        pastOrderId = "";

        cartItems = new ArrayList<>();

        payNowLinearLayout = view.findViewById(R.id.payNowLinearLayout);
        changeDeliveryAddressLayout = view.findViewById(R.id.changeDeliveryAddressLayout);

        cancelCheckout = view.findViewById(R.id.cancelCheckout);

        addItemsCheckout = view.findViewById(R.id.addItemsCheckout);
        subtotalPriceCheckout = view.findViewById(R.id.subtotalPriceCheckout);
        deliveryFeePriceCheckout = view.findViewById(R.id.deliveryFeePriceCheckout);
        taxesPriceCheckout = view.findViewById(R.id.taxesPriceCheckout);
        totalPricePriceCheckout = view.findViewById(R.id.totalPricePriceCheckout);
        totalCheckoutPrice = view.findViewById(R.id.totalCheckoutPrice);
        deliveryAddressTv = view.findViewById(R.id.deliveryAddressTv);
        deliveryProvinceTv = view.findViewById(R.id.deliveryProvinceTv);

        checkoutItemsRecycler = view.findViewById(R.id.checkoutItemsRecycler);
        cartItemsAdapter = new CartItemsAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        checkoutItemsRecycler.setLayoutManager(linearLayoutManager);
        checkoutItemsRecycler.setAdapter(cartItemsAdapter);

        getCartItems();
        getUserItems();

        setOnCLickListeners();
    }

    private void getCartItems(){
        cartViewModel = new ViewModelProvider(this.requireActivity()).get(CartViewModel.class);
        cartViewModel.getCartData(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                cartItems = cartModels;
                cartItemsAdapter.setCartModels(cartModels);
                insertCorrectData(cartModels);
            }
        });
    }

    private void getUserItems() {
        userViewModel = new ViewModelProvider(this.requireActivity()).get(UserViewModel.class);
        userViewModel.getUserModelData(getActivity()).observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                thisUserModel = userModel;
                if(userModel.getDeliveryAddress() != null) deliveryAddress = userModel.getDeliveryAddress();
                else deliveryAddress = "Add a delivery address";
                if(userModel.getDeliveryProvince() != null) deliveryProvince = userModel.getDeliveryProvince();
                else deliveryProvince = "";
                deliveryAddressTv.setText(deliveryAddress);
                deliveryProvinceTv.setText(deliveryProvince);
            }
        });
    }

    private void insertCorrectData(List<CartModel> cartModels) {
        subTotalPrice = 0.00;
        for(CartModel cartModel: cartModels){
            subTotalPrice += cartModel.getTotalPrice();
        }
        subtotalPriceCheckout.setText("$" + round(subTotalPrice, 2));

        deliveryFeePriceCheckout.setText("$" + round(deliveryFee, 2));

        taxesPriceCheckout.setText("$" + round(taxes, 2));

        totalPrice = subTotalPrice + deliveryFee + taxes;
        totalPricePriceCheckout.setText("$" + round(totalPrice, 2));

        totalCheckoutPrice.setText("$" + round(totalPrice, 2));
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void getInfoToAddToPastOrdersList() {
        String image = cartItems.get(0).getImage();
        int numberOfItems = 0;
        for(CartModel cartModel: cartItems){
            numberOfItems = numberOfItems + cartModel.getNumOrders();
        }
        Double totalPrice = 0.0;
        for(CartModel cartModel: cartItems){
            totalPrice = totalPrice + cartModel.getTotalPrice();
        }
        orderNumber = thisUserModel.getTotalNumberOfOrders() + 1;
        addToPastOrdersCollection(image, false, numberOfItems, orderNumber, totalPrice);
    }

    public void addToPastOrdersCollection(String image, boolean liked, int numberOfItems,
                                           int orderNumber, Double totalPrice){
        HashMap<String, Object> pastOrderMap = new HashMap<>();
        pastOrderMap.put("image", image);
        pastOrderMap.put("liked", liked);
        pastOrderMap.put("numberOfItems", numberOfItems);
        pastOrderMap.put("orderNumber", orderNumber);
        pastOrderMap.put("totalPrice", totalPrice);
        pastOrderMap.put("timestamp", new Date());

        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .collection("Past Orders")
                .add(pastOrderMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            pastOrderId = documentReference.getId();
                            addOrderSpecificDetails();
                            clearCart();
                            updateTotalNumberOfOrders();
                        }
                    }
                });
    }

    private void addOrderSpecificDetails() {
        for(CartModel cartModel: cartItems){
            HashMap<String, Object> orderSpecificDetailsMap = new HashMap<>();
            boolean liked = cartModel.isLiked();
            String mealId = cartModel.getMealId();
            String image = cartModel.getImage();
            String nameOfMeal = cartModel.getNameOfMeal();
            int numOrders = cartModel.getNumOrders();
            Double totalPrice = cartModel.getTotalPrice();

            orderSpecificDetailsMap.put("liked", liked);
            orderSpecificDetailsMap.put("mealId", mealId);
            orderSpecificDetailsMap.put("image", image);
            orderSpecificDetailsMap.put("nameOfMeal", nameOfMeal);
            orderSpecificDetailsMap.put("numOrders", numOrders);
            orderSpecificDetailsMap.put("totalPrice", totalPrice);

            firebaseFirestore
                    .collection("Users")
                    .document(account.getId())
                    .collection("Past Orders")
                    .document(pastOrderId)
                    .collection("Order Specific Details")
                    .add(orderSpecificDetailsMap)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    });
        }

    }

    private void clearCart() {
        for(int i=0; i<cartItems.size(); i++){
            onRemoveItemClicked(i);
        }
    }

    private void updateTotalNumberOfOrders() {
        HashMap<String, Object> totalNumberOfOrdersMap = new HashMap<>();
        int totalNumberOfOrders = orderNumber;
        totalNumberOfOrdersMap.put("totalNumberOfOrders", totalNumberOfOrders);
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .update(totalNumberOfOrdersMap);
    }

    private void setOnCLickListeners() {
        addItemsCheckout.setOnClickListener(this);
        cancelCheckout.setOnClickListener(this);
        changeDeliveryAddressLayout.setOnClickListener(this);
        payNowLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cancelCheckout:
                dismiss();
                break;
            case R.id.changeDeliveryAddressLayout:
                Intent i = new Intent(getActivity(), SearchDeliveryAddressActivity.class);
                startActivity(i);
                break;
            case R.id.payNowLinearLayout:
                if(totalPrice>0.0)
                getInfoToAddToPastOrdersList();
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