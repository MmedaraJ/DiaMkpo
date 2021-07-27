package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.Adapters.HighestRatedMealsAdapter;
import com.example.diamkpo.Adapters.OurCustomersFavouritesAdapter;
import com.example.diamkpo.Adapters.YourFavouritesAdapter;
import com.example.diamkpo.ItemDecorations.GridSpacingItemDecoration;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.PastOrderModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.example.diamkpo.ViewModels.MealViewModel;
import com.example.diamkpo.ViewModels.PastOrderViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerHomeFragment extends Fragment implements View.OnClickListener {
    private ConstraintLayout lastMealBackground;

    private ImageView menuIconCustomerHome;
    private ImageView cartIconCustomerHome;
    private ImageView yourLastMealImage;
    private ImageView lastMealLikeCircle;

    private TextView customerHomeHeaderTv;
    private TextView reorderTvYourLastMeal;
    private TextView lastMealOrderNumber;
    private TextView lastMealNumberOfItems;
    private TextView lastMealPrice;
    private TextView lastMealDate;
    private TextView lastMealCompletionStatus;
    private TextView lastMealDivider;
    private TextView lastMealDivider2;

    private RecyclerView highestRatedMealsRecycler;
    private RecyclerView yourFavouritesRecycler;
    private RecyclerView ourCustomersFavouritesRecycler;

    private HighestRatedMealsAdapter highestRatedMealsAdapter;
    private YourFavouritesAdapter yourFavouritesAdapter;
    private OurCustomersFavouritesAdapter ourCustomersFavouritesAdapter;

    private MealViewModel mealViewModel;
    private PastOrderViewModel pastOrderViewModel;

    private NavController navController;

    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private PastOrderModel lastOrder;

    private static final String TAG = "CUSTOMER_HOME_LOG";

    private int currentHour;

    private String currentUserId;
    private String mealId;

    private List<MealModel> fourBestMealsList;
    private List<MealModel> allMealsList;
    private List<MealModel> favouriteMealsList;

    private HashMap<MealModel, Long> favouriteMealsMap;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        fourBestMealsList = new ArrayList<>();
        allMealsList = new ArrayList<>();
        favouriteMealsList = new ArrayList<>();

        favouriteMealsMap = new HashMap<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = "";

        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            currentUserId = account.getId();
        }

        lastMealBackground = view.findViewById(R.id.lastMealBackground);

        //image views
        menuIconCustomerHome = view.findViewById(R.id.menuIconCustomerHome);
        cartIconCustomerHome = view.findViewById(R.id.cartIconCustomerHome);
        yourLastMealImage = view.findViewById(R.id.yourLastMealImage);
        lastMealLikeCircle = view.findViewById(R.id.lastMealLikeCircle);

        //text views
        customerHomeHeaderTv = view.findViewById(R.id.customerHomeHeaderTv);
        reorderTvYourLastMeal = view.findViewById(R.id.reorderTvYourLastMeal);
        lastMealOrderNumber = view.findViewById(R.id.lastMealOrderNumber);
        lastMealNumberOfItems = view.findViewById(R.id.lastMealNumberOfItems);
        lastMealPrice = view.findViewById(R.id.lastMealPrice);
        lastMealDate = view.findViewById(R.id.lastMealDate);
        lastMealCompletionStatus = view.findViewById(R.id.lastMealCompletionStatus);
        lastMealDivider = view.findViewById(R.id.lastMealDivider);
        lastMealDivider2 = view.findViewById(R.id.lastMealDivider2);

        //highest rated MealModel recycler and adapter
        highestRatedMealsRecycler = view.findViewById(R.id.highestRatedMealsRecycler);
        highestRatedMealsAdapter = new HighestRatedMealsAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        int spanCount = 2;
        int spacing = 10;
        highestRatedMealsRecycler.setLayoutManager(gridLayoutManager);
        highestRatedMealsRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        highestRatedMealsRecycler.setAdapter(highestRatedMealsAdapter);

        //your favourites recycler and adapter
        yourFavouritesRecycler = view.findViewById(R.id.yourFavouritesRecycler);
        yourFavouritesAdapter = new YourFavouritesAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        yourFavouritesRecycler.setLayoutManager(linearLayoutManager);
        yourFavouritesRecycler.setAdapter(yourFavouritesAdapter);

        currentHour = getHourOfDay();
        Log.d(TAG, "---------------------------------------- current hour is " + currentHour);

        setUpHighestRatedMealsAdapter(currentHour);

        navController = Navigation.findNavController(view);

        //our customers favourites recycler and adapter
        ourCustomersFavouritesRecycler = view.findViewById(R.id.ourCustomersFavouritesRecycler);
        ourCustomersFavouritesAdapter = new OurCustomersFavouritesAdapter(getActivity());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        ourCustomersFavouritesRecycler.setLayoutManager(linearLayoutManager1);
        ourCustomersFavouritesRecycler.setAdapter(ourCustomersFavouritesAdapter);

        getHighestRatedMeals();

        getLastOrder();

        setOnClickListeners();
    }

    private void getHighestRatedMeals() {
        mealViewModel = new ViewModelProvider(this.requireActivity()).get(MealViewModel.class);
        mealViewModel.getHighestRatedMealsModelData().observe(getViewLifecycleOwner(), new Observer<List<MealModel>>() {
            @Override
            public void onChanged(List<MealModel> mealModelList) {
                addClickSupportToRecycler(ourCustomersFavouritesRecycler, mealModelList);
                ourCustomersFavouritesAdapter.setMealModels(mealModelList);
            }
        });
    }

    private void getLastOrder() {
        pastOrderViewModel = new ViewModelProvider(this.requireActivity()).get(PastOrderViewModel.class);
        pastOrderViewModel.getPastOrderData(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<PastOrderModel>>() {
            @Override
            public void onChanged(List<PastOrderModel> pastOrderModels) {
                if(pastOrderModels.size() > 0) {
                    lastOrder = pastOrderModels.get(0);
                    if (lastOrder != null) {
                        updateLastMealUI();
                    }
                }
            }
        });
    }

    private void updateLastMealUI() {
        Glide.with(getActivity())
                .load(lastOrder.getImage())
                .centerCrop()
                .placeholder(R.drawable.burger1)
                .into(yourLastMealImage);

        lastMealOrderNumber.setText("#" + lastOrder.getOrderNumber());

        if(lastOrder.isLiked()){
            lastMealLikeCircle.setBackgroundResource(R.drawable.red_heart_shape);
        }else{
            lastMealLikeCircle.setBackgroundResource(0);
        }

        String noOfItems = "";
        if(lastOrder.getNumberOfItems()<2){
            noOfItems = lastOrder.getNumberOfItems() + " item";
        }else{
            noOfItems = lastOrder.getNumberOfItems() + " items";
        }
        lastMealNumberOfItems.setText(noOfItems);

        lastMealPrice.setText("$" + round(lastOrder.getTotalPrice(), 2));

        String pattern = "MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(lastOrder.getTimestamp());
        lastMealDate.setText("" + date);

        lastMealCompletionStatus.setText(R.string.past_order_completion_status);
        lastMealDivider.setText("|");
        lastMealDivider2.setText("|");
    }

    private void setOnClickListeners() {
        cartIconCustomerHome.setOnClickListener(this);
        menuIconCustomerHome.setOnClickListener(this);
        reorderTvYourLastMeal.setOnClickListener(this);
        lastMealBackground.setOnClickListener(this);
    }

    private void getFavouriteMeals(){
        for(int i = 0; i < fourBestMealsList.size(); i++) {
            firebaseFirestore
                    .collection("meals")
                    .document(fourBestMealsList.get(i).getMealId())
                    .collection("Number of Orders")
                    .document(currentUserId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null && documentSnapshot.exists()){
                                    Double numOrders = documentSnapshot.getDouble("numOrders");
                                }
                            }
                        }
                    });
        }
    }

    private void addClickSupportToRecycler(RecyclerView recycler, List<MealModel> mealModelList){
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                navigateToProductPage(mealModelList, position);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                mealId = mealModelList.get(position).getMealId();
                reactToDoubleClick(mealId, mealModelList, position);
            }
        });
    }

    private void reactToDoubleClick(String mealId, List<MealModel> mealModelList, int position){
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Liked")
                .document(currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists()) {
                                boolean liked = documentSnapshot.getBoolean("liked");
                                setNewLike(mealId, !liked, mealModelList, position);
                            }
                        }
                    }
                });
    }

    private void navigateToProductPage(List<MealModel> mealModels, int position){
        CustomerHomeFragmentDirections.ActionCustomerHomeFragmentToProductPageFragment action = CustomerHomeFragmentDirections.actionCustomerHomeFragmentToProductPageFragment();
        action.setMealId(mealModels.get(position).getMealId());
        navController.navigate(action);
    }

    /**
    * reach to a high rated meal being liked
    */
    private void setNewLike(String mealId, boolean liked, List<MealModel> mealModelList, int position){
        HashMap<String, Object> likedMap = new HashMap<>();
        likedMap.put("liked", liked);
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Liked")
                .document(currentUserId)
                .set(likedMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isComplete()){
                    highestRatedMealsAdapter.notifyDataSetChanged();
                    ourCustomersFavouritesAdapter.notifyDataSetChanged();
                    yourFavouritesAdapter.notifyDataSetChanged();
                    if(liked) updateLikedMeals(mealId, mealModelList, position);
                    else deleteLikedMeal(mealId);
                }
            }
        });
    }

    private void deleteLikedMeal(String mealId) {
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Liked Meals")
                .document(mealId)
                .delete();
    }

    private void updateLikedMeals(String mealId, List<MealModel> mealModelList, int position) {
        Map<String, Object> likedMealMap = new HashMap<>();
        likedMealMap.put("mealId", mealModelList.get(position).getMealId());
        likedMealMap.put("name", mealModelList.get(position).getName());
        likedMealMap.put("image", mealModelList.get(position).getImage());

        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Liked Meals")
                .document(mealId)
                .set(likedMealMap);
    }

    /**
    * Display appropriate greeting depending on time of day
    * @param currentHour is the current hour
    */
    private void setUpHighestRatedMealsAdapter(int currentHour){
        if(currentHour > -1 && currentHour < 4){
            customerHomeHeaderTv.setText(R.string.customer_evening_home_screen_header);
            getFourMealsBasedOnCategory("Dinner");
        }
        else if(currentHour > 3 && currentHour < 12){
            customerHomeHeaderTv.setText(R.string.customer_morning_home_screen_header);
            getFourMealsBasedOnCategory("Breakfast");
        }
        else if(currentHour > 11 && currentHour < 17){
            customerHomeHeaderTv.setText(R.string.customer_afternoon_home_screen_header);
            getFourMealsBasedOnCategory("Lunch");
        }
        else if(currentHour > 16 && currentHour < 24){
            customerHomeHeaderTv.setText(R.string.customer_evening_home_screen_header);
            getFourMealsBasedOnCategory("Dinner");
        }
    }

    private void getFourMealsBasedOnCategory(String category) {
        mealViewModel = new ViewModelProvider(this.requireActivity()).get(MealViewModel.class);
        mealViewModel.getMealBasedOnTimeOfDay(category).observe(getViewLifecycleOwner(), new Observer<List<MealModel>>() {
            @Override
            public void onChanged(List<MealModel> mealModelList) {
                Collections.shuffle(mealModelList);
                fourBestMealsList = mealModelList.subList(0, 4);
                addClickSupportToRecycler(highestRatedMealsRecycler, mealModelList.subList(0, 4));
                addClickSupportToRecycler(yourFavouritesRecycler, mealModelList);
                highestRatedMealsAdapter.setMealsList(mealModelList.subList(0, 4));
                yourFavouritesAdapter.setMealModels(mealModelList);
            }
        });
    }

    /**
    * get current hour in 24 format
    * @return current hour in 24 format
    */
    private int getHourOfDay(){
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.HOUR_OF_DAY);
    }

    private void addToCart() {
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Past Orders")
                .document(lastOrder.getPastOrderId())
                .collection("Order Specific Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                HashMap<String, Object> cartItem = new HashMap<>();

                                //TODO Set up liking ability
                                boolean liked = document.getBoolean("liked");
                                String nameOfMeal = document.getString("nameOfMeal");
                                Double numOrders = document.getDouble("numOrders");
                                Double totalPrice = document.getDouble("totalPrice");

                                cartItem.put("liked", liked);
                                cartItem.put("nameOfMeal", nameOfMeal);
                                cartItem.put("numOrders", numOrders);
                                cartItem.put("totalPrice", totalPrice);

                                firebaseFirestore.collection("Users")
                                        .document(currentUserId)
                                        .collection("Cart")
                                        .add(cartItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()) {
                                            openCart();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void openCart(){
        CartBottomSheetFragment cartBottomSheetFragment = new CartBottomSheetFragment();
        cartBottomSheetFragment.show(getParentFragmentManager(), cartBottomSheetFragment.getTag());
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void navigateToPastOrderDetailPage() {
        CustomerHomeFragmentDirections.ActionCustomerHomeFragmentToPastOrderDetailsFragment action = CustomerHomeFragmentDirections.actionCustomerHomeFragmentToPastOrderDetailsFragment();
        action.setPastOrderId(lastOrder.getPastOrderId());
        action.setOrderNumber(lastOrder.getOrderNumber());
        action.setTotalPrice(lastOrder.getTotalPrice() + "");
        navController.navigate(action);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cartIconCustomerHome:
                openCart();
                break;
            case R.id.menuIconCustomerHome:
                navController.navigate(CustomerHomeFragmentDirections.actionCustomerHomeFragmentToMenuFragment());
                break;
            case R.id.reorderTvYourLastMeal:
                addToCart();
                break;
            case R.id.lastMealBackground:
                navigateToPastOrderDetailPage();
                break;
        }
    }
}