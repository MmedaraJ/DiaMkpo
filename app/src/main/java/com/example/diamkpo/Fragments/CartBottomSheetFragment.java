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
    private MealViewModel mealViewModel;
    private UserViewModel userViewModel;

    private UserModel thisUserModel;

    private List<CartModel> cartItems;
    private List<MealModel> meals;

    private TextView goToCheckoutTv;
    private TextView cartTotalPrice;

    private Double totalPrice;

    private String pastOrderId;
    private String image;

    private int orderNumber;

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

        pastOrderId = "";

        cartItems = new ArrayList<>();
        meals = new ArrayList<>();

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
        getMeals();
        getUserItems();

        addOnClickListeners();
    }

    private void getUserItems() {
        userViewModel = new ViewModelProvider(this.requireActivity()).get(UserViewModel.class);
        userViewModel.getUserModelData(getActivity()).observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                thisUserModel = userModel;
            }
        });
    }

    private void getCartItems(){
        cartViewModel = new ViewModelProvider(this.requireActivity()).get(CartViewModel.class);
        cartViewModel.getCartData(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                cartItems = cartModels;
                //addClickSupportToRecycler(cartItemsRecycler, cartModels);
                cartItemsAdapter.setCartModels(cartModels);
                setCorrectTotalPrice(cartModels);
            }
        });
    }

    private void getMeals(){
        mealViewModel = new ViewModelProvider(this.requireActivity()).get(MealViewModel.class);
        mealViewModel.getMealsModelData().observe(getViewLifecycleOwner(), new Observer<List<MealModel>>() {
            @Override
            public void onChanged(List<MealModel> mealModels) {
                meals = mealModels;
            }
        });
    }

    private void addClickSupportToRecycler(RecyclerView recycler, List<CartModel> cartModelList){
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(TAG, "/-/-/-/-/-/-/-/-/-/-/-/-/-/---/-/-/-/-/-/ " + cartModelList.get(position).getNameOfMeal());
                openProductPage(cartModelList, position);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                /*mealId = mealModelList.get(position).getMealId();
                reactToDoubleClick(mealId);*/
            }
        });
    }

    private void openProductPage(List<CartModel> cartModelList, int position){
        Log.d(TAG, "/-/-/-/-/-/-/-/-/-/-/-/-/-/---/-/-/-/-/-/ " + cartModelList.get(position).getNameOfMeal());
        ProductPageFragment productPageFragment = new ProductPageFragment();
        productPageFragment.setMealId(cartModelList.get(position).getMealId());
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

    private void openCart(){
        CheckoutBottomSheetFragment checkoutBottomSheetFragment = new CheckoutBottomSheetFragment(cartItems);
        checkoutBottomSheetFragment.show(getParentFragmentManager(), checkoutBottomSheetFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goToCheckoutTv:
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