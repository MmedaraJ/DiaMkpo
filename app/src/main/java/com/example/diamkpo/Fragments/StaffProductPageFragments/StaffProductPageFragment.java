package com.example.diamkpo.Fragments.StaffProductPageFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.Adapters.AddNewCategoryAdapter;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.diamkpo.Fragments.ProductPageFragment.round;

public abstract class StaffProductPageFragment extends Fragment implements View.OnClickListener, AddNewCategoryAdapter.NewCategoryItemAdded {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private NavController navController;

    private ImageView backIconStaffProductPageFragment;
    private ImageView productImageStaffProductPage;
    private ImageView addCategory;
    private ImageView removeCategory;

    private EditText staffProductPageHeader;
    private EditText staffProductDescriptionProductPage;
    private EditText staffProductPageNormalPrice;
    private EditText staffProductPageDealPrice;
    private EditText productVisibility;
    private EditText staffProductImageProductPage;

    private FloatingActionButton doneEditingProduct;
    private FloatingActionButton deleteProduct;

    private RecyclerView newCategoryRecycler;

    private AddNewCategoryAdapter addNewCategoryAdapter;

    private final static String TAG = "Staff Product Page";

    private String mealId;
    private String mealImage;
    private String mealName;
    private String mealDescription;
    private String visibility;
    private String mealImageLink;

    private Double normalPrice;
    private Double dealPrice;

    private List<String> allNewCategories;

    private boolean addNew;

    public StaffProductPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_product_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        navController = Navigation.findNavController(view);

        backIconStaffProductPageFragment = view.findViewById(R.id.backIconStaffProductPageFragment);
        productImageStaffProductPage = view.findViewById(R.id.productImageStaffProductPage);
        addCategory = view.findViewById(R.id.addCategory);
        removeCategory = view.findViewById(R.id.removeCategory);

        staffProductPageHeader = view.findViewById(R.id.staffProductPageHeader);
        staffProductDescriptionProductPage = view.findViewById(R.id.staffProductDescriptionProductPage);
        staffProductPageNormalPrice = view.findViewById(R.id.staffProductPageNormalPrice);
        staffProductPageDealPrice = view.findViewById(R.id.staffProductPageDealPrice);
        productVisibility = view.findViewById(R.id.productVisibility);
        staffProductImageProductPage = view.findViewById(R.id.staffProductImageProductPage);

        doneEditingProduct = view.findViewById(R.id.doneEditingProduct);
        deleteProduct = view.findViewById(R.id.deleteProduct);

        //cart MealModel recycler and adapter
        newCategoryRecycler = view.findViewById(R.id.newCategoryRecycler);
        addNewCategoryAdapter = new AddNewCategoryAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newCategoryRecycler.setLayoutManager(linearLayoutManager);
        newCategoryRecycler.setAdapter(addNewCategoryAdapter);

        setOnClickListeners();

        mealId = "";
        mealImage = "";
        mealName = "";
        mealDescription = "";
        visibility = "";
        mealImageLink = "";

        normalPrice = 0.0;
        dealPrice = 0.0;

        addNew = false;

        allNewCategories = new ArrayList<>();

        inputCorrectData();
    }

    private void setOnClickListeners(){
        backIconStaffProductPageFragment.setOnClickListener(this);
        productImageStaffProductPage.setOnClickListener(this);
        addCategory.setOnClickListener(this);
        removeCategory.setOnClickListener(this);
        doneEditingProduct.setOnClickListener(this);
        deleteProduct.setOnClickListener(this);
    }

    protected abstract void inputCorrectData();

    public void emptyEditTexts(){
        allNewCategories.add("");
        addNewCategoryAdapter.setAllNewCategories(allNewCategories);
    }

    public void populateEditTexts(){
        mealId = UpdateProductFragmentArgs.fromBundle(getArguments()).getMealId();
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists() && documentSnapshot != null) {
                            mealName = documentSnapshot.getString("name");
                            staffProductPageHeader.setText(mealName);
                            mealImage = documentSnapshot.getString("image");
                            Glide.with(getActivity())
                                    .load(mealImage)
                                    .centerCrop()
                                    .placeholder(R.drawable.burger1)
                                    .into(productImageStaffProductPage);
                            mealImageLink = documentSnapshot.getString("image");
                            staffProductImageProductPage.setText(mealImageLink);
                            mealDescription = documentSnapshot.getString("description");
                            staffProductDescriptionProductPage.setText(mealDescription);
                            normalPrice = round(documentSnapshot.getDouble("normalPrice"), 2);
                            staffProductPageNormalPrice.setText("$" + normalPrice);
                            dealPrice = round(documentSnapshot.getDouble("dealPrice"), 2);
                            staffProductPageDealPrice.setText("$" + dealPrice);
                            allNewCategories = (List<String>) documentSnapshot.get("category");
                            allNewCategories.add("");
                            addNewCategoryAdapter.setAllNewCategories(allNewCategories);
                            visibility = documentSnapshot.getString("visibility");
                            productVisibility.setText(visibility);
                        }
                    }
                });
    }

    public void getUpdatedMealInfo(){
        if(!staffProductPageHeader.getText().toString().isEmpty() &&
                !staffProductImageProductPage.getText().toString().isEmpty() &&
                !staffProductDescriptionProductPage.getText().toString().isEmpty() &&
                !staffProductPageNormalPrice.getText().toString().isEmpty() &&
                !staffProductPageDealPrice.getText().toString().isEmpty() &&
                !allNewCategories.isEmpty() &&
                !productVisibility.getText().toString().isEmpty()) {

            Double avgRating = 0.0;
            int numOrders = 0;
            int numRatings = 0;
            String updatedName = staffProductPageHeader.getText().toString();
            String updatedImage = staffProductImageProductPage.getText().toString();
            String updatedDescription = staffProductDescriptionProductPage.getText().toString();
            String updatedNormalPriceString = staffProductPageNormalPrice.getText().toString();
            Double updatedNormalPrice = Double.valueOf(updatedNormalPriceString.substring(1));
            String updatedDealPriceString = staffProductPageDealPrice.getText().toString();
            Double updatedDealPrice = Double.valueOf(updatedDealPriceString.substring(1));
            String updatedVisibility = productVisibility.getText().toString();

            for (int i = 0; i < allNewCategories.size(); i++) {
                if (allNewCategories.get(i).equals("")) {
                    allNewCategories.remove(i);
                    i--;
                }
            }

            Map<String, Object> updatedMealInfo = new HashMap<>();
            updatedMealInfo.put("name", updatedName);
            updatedMealInfo.put("image", updatedImage);
            updatedMealInfo.put("description", updatedDescription);
            updatedMealInfo.put("normalPrice", updatedNormalPrice);
            updatedMealInfo.put("dealPrice", updatedDealPrice);
            updatedMealInfo.put("category", allNewCategories);
            updatedMealInfo.put("visibility", updatedVisibility);

            if (addNew) {
                updatedMealInfo.put("avgRating", avgRating);
                updatedMealInfo.put("numOrders", numOrders);
                updatedMealInfo.put("numRatings", numRatings);
                addNewMeal(updatedMealInfo);
            }else{
                updateMeal(updatedMealInfo);
            }
        }
    }

    public void addNewMeal(Map<String, Object> updatedMealInfo){
        firebaseFirestore
                .collection("meals")
                .document()
                .set(updatedMealInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        //close staff product page
                        //TODO popBackStack when you understand it. Many places need this
                        navigateToMenu(navController);
                    }
                });
    }

    public void updateMeal(Map<String, Object> updatedMealInfo){
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .update(updatedMealInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //close staff product page
                            //TODO popBackStack when you understand it. Many places need this
                            navigateToMenu(navController);
                        }
                    }
                });
    }

    public abstract void navigateToMenu(NavController navController);

    public void deleteMeal(){
        firebaseFirestore
                .collection("meals")
                .document(mealId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        //close staff product page
                        //TODO popBackStack when you understand it. Many places need this
                        navigateToMenu(navController);
                    }
                });
    }

    private void addNewCategory(){
        allNewCategories.add("");
        addNewCategoryAdapter.setAllNewCategories(allNewCategories);
    }

    @Override
    public void onItemAdded(int position, String s){
        if(!s.isEmpty()) {
            if(position>-1) {
                if(!allNewCategories.contains(s)) {
                    allNewCategories.set(position, s);
                    addNewCategoryAdapter.setAllNewCategories(allNewCategories);
                }
            }
        }
    }

    @Override
    public abstract void onClick(View v);

    public void addProductOnClick(View v){
        switch(v.getId()){
            case R.id.addCategory:
                if(addCategory.requestFocus())
                    addNewCategory();
                break;
            case R.id.removeCategory:
                if(allNewCategories.size()>1)
                    allNewCategories.remove(allNewCategories.size()-1);
                addNewCategoryAdapter.setAllNewCategories(allNewCategories);
                break;
            case R.id.doneEditingProduct:
                addNew = true;
                getUpdatedMealInfo();
                break;
            case R.id.deleteProduct:
                navigateToMenu(navController);
                break;
        }
    }

    public void updateProductOnClick(View v){
        switch(v.getId()){
            case R.id.addCategory:
                if(addCategory.requestFocus())
                    addNewCategory();
                break;
            case R.id.removeCategory:
                if(allNewCategories.size()>1)
                    allNewCategories.remove(allNewCategories.size()-1);
                addNewCategoryAdapter.setAllNewCategories(allNewCategories);
                break;
            case R.id.doneEditingProduct:
                addNew = false;
                getUpdatedMealInfo();
                break;
            case R.id.deleteProduct:
                deleteMeal();
                break;
        }
    }
}