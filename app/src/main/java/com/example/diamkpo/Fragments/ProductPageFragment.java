package com.example.diamkpo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class ProductPageFragment extends Fragment implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private NavController navController;

    private ImageView backIconProductPageFragment;
    private ImageView productImageProductPage;
    private ImageView shareIconProductPageFragment;
    private ImageView productPageLikeButton;

    private TextView productPageHeader;
    private TextView productDescriptionProductPage;
    private TextView totalGeneralOrdersTv;
    private TextView totalPersonalOrdersTv;
    private TextView specialInstructionsHeaderTv;
    private TextView minusSignProductPage;
    private TextView numberOfProductsProductPage;
    private TextView plusSignProductPage;
    private TextView productPriceProductPage;
    private TextView addToCartTvProductPage;

    private EditText specialInstructionsEditText;

    private String mealId;
    private String mealImage;
    private String currentUserId;

    private Double price;
    private Double singlePrice;

    private int currentNumProducts;

    private boolean isLiked;

    public ProductPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        currentUserId = "";
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            currentUserId = account.getId();
        }else {
        }

        navController = Navigation.findNavController(view);

        backIconProductPageFragment = view.findViewById(R.id.backIconProductPageFragment);
        productImageProductPage = view.findViewById(R.id.productImageProductPage);
        shareIconProductPageFragment = view.findViewById(R.id.shareIconProductPageFragment);
        productPageLikeButton = view.findViewById(R.id.productPageLikeButton);

        productPageHeader = view.findViewById(R.id.productPageHeader);
        productDescriptionProductPage = view.findViewById(R.id.productDescriptionProductPage);
        totalGeneralOrdersTv = view.findViewById(R.id.totalGeneralOrdersTv);
        totalPersonalOrdersTv = view.findViewById(R.id.totalPersonalOrdersTv);
        specialInstructionsHeaderTv = view.findViewById(R.id.noteHeaderTv);
        minusSignProductPage = view.findViewById(R.id.minusSignProductPage);
        numberOfProductsProductPage = view.findViewById(R.id.numberOfProductsProductPage);
        numberOfProductsProductPage.setText("1");
        plusSignProductPage = view.findViewById(R.id.plusSignProductPage);
        productPriceProductPage = view.findViewById(R.id.productPriceProductPage);
        addToCartTvProductPage = view.findViewById(R.id.addToCartTvProductPage);

        specialInstructionsEditText = view.findViewById(R.id.staffEmailEditText);

        setOnClickListeners();

        mealId = ProductPageFragmentArgs.fromBundle(getArguments()).getMealId();

        price = 0.0;
        singlePrice = 0.0;

        currentNumProducts = Integer.parseInt((String) numberOfProductsProductPage.getText());

        inputCorrectData(mealId);
    }

    public void setMealId(String mealId){
        this.mealId = mealId;
        inputCorrectData(mealId);
    }

    private void setOnClickListeners() {
        shareIconProductPageFragment.setOnClickListener(this);
        minusSignProductPage.setOnClickListener(this);
        plusSignProductPage.setOnClickListener(this);
        addToCartTvProductPage.setOnClickListener(this);
        backIconProductPageFragment.setOnClickListener(this);
    }

    private void inputCorrectData(String mealId) {
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists() && documentSnapshot != null) {
                            productPageHeader.setText(documentSnapshot.getString("name"));
                            mealImage = documentSnapshot.getString("image");
                            Glide.with(getActivity())
                                    .load(mealImage)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder_image)
                                    .into(productImageProductPage);
                            productDescriptionProductPage.setText(documentSnapshot.getString("description"));
                            String genOrders = documentSnapshot.getDouble("numOrders") + " people have ordered this meal";
                            totalGeneralOrdersTv.setText(genOrders);
                            singlePrice = round(documentSnapshot.getDouble("normalPrice"), 2);
                            price = singlePrice * currentNumProducts;
                            productPriceProductPage.setText("$" + price);
                            setLike(mealId);
                            getPersonalNumOrders();
                        }
                    }
                });
    }

    private void setLike(String mealId) {
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Liked")
                .document(currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            boolean liked = documentSnapshot.getBoolean("liked");
                            isLiked = liked;
                            if(liked){
                                productPageLikeButton.setBackgroundResource(R.drawable.red_heart_shape);
                            }else{
                                productPageLikeButton.setBackgroundResource(0);
                            }
                        }
                    }
                });
    }

    private void getPersonalNumOrders() {
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .collection("Number of Orders")
                .document(currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists() && documentSnapshot != null) {
                            String personalOrders = "You have ordered this " + documentSnapshot.getDouble("numOrders") + " times";
                            totalPersonalOrdersTv.setText(personalOrders);
                        }
                    }
                });
    }

    private void addToCart() {
        HashMap<String, Object> cartItem = new HashMap<>();

        //TODO Set up liking ability
        String nameOfMeal = (String) productPageHeader.getText();
        int numOrders = Integer.parseInt((String) numberOfProductsProductPage.getText());
        Double totalPrice = singlePrice * numOrders;

        cartItem.put("image", mealImage);
        cartItem.put("mealId", mealId);
        cartItem.put("liked", isLiked);
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

    private void openCart(){
        CartBottomSheetFragment cartBottomSheetFragment = new CartBottomSheetFragment();
        cartBottomSheetFragment.show(getParentFragmentManager(), cartBottomSheetFragment.getTag());
    }

    private void openShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = mealImage;
        String shareSub = "Check out this meal from Dia Mkpo";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

    private void updatePrice(){
        price = round(singlePrice * currentNumProducts, 2);
        productPriceProductPage.setText("$" + price);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.plusSignProductPage:
                currentNumProducts++;
                numberOfProductsProductPage.setText(String.valueOf(currentNumProducts));
                updatePrice();
                break;
            case R.id.minusSignProductPage:
                if(currentNumProducts>1) {
                    currentNumProducts--;
                    numberOfProductsProductPage.setText(String.valueOf(currentNumProducts));
                    updatePrice();
                }
                break;
            case R.id.addToCartTvProductPage:
                addToCart();
                break;
            case R.id.shareIconProductPageFragment:
                openShareIntent();
                break;
            case R.id.backIconProductPageFragment:
                navController.popBackStack();
                break;
        }
    }
}