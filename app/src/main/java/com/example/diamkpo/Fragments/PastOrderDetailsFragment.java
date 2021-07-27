package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.Adapters.PastOrderDetailsCartItemsAdapter;
import com.example.diamkpo.Adapters.PastOrderItemsAdapter;
import com.example.diamkpo.Adapters.RateItemsAdapter;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.example.diamkpo.ViewModels.OrderSpecificDetailsViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PastOrderDetailsFragment extends Fragment implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private NavController navController;

    private ImageView backIconPastOrderDetails;

    private TextView pastOrderDetailsOrderNumber;
    private TextView pastOrderDetailsTimestamp;
    private TextView rateOrderPastOrderDetails;
    private TextView totalPricePastOrderDetails;

    private Button reorderButtonPastOrderDetails;

    private RecyclerView pastOrderDetailsCartItemsRecycler;

    private PastOrderDetailsCartItemsAdapter pastOrderDetailsCartItemsAdapter;

    private OrderSpecificDetailsViewModel orderSpecificDetailsViewModel;

    private String pastOrderId;
    private String totalPrice;
    private String timestamp;

    private int orderNumber;

    private boolean liked;

    private List<OrderSpecificDetailModel> orderSpecificDetailModelList;

    public PastOrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        navController = Navigation.findNavController(view);

        pastOrderId = PastOrderDetailsFragmentArgs.fromBundle(getArguments()).getPastOrderId();
        orderNumber = PastOrderDetailsFragmentArgs.fromBundle(getArguments()).getOrderNumber();
        totalPrice = PastOrderDetailsFragmentArgs.fromBundle(getArguments()).getTotalPrice();
        timestamp = PastOrderDetailsFragmentArgs.fromBundle(getArguments()).getTimestamp();
        liked = PastOrderDetailsFragmentArgs.fromBundle(getArguments()).getLiked();

        backIconPastOrderDetails = view.findViewById(R.id.backIconPastOrderDetails);

        pastOrderDetailsOrderNumber = view.findViewById(R.id.pastOrderDetailsOrderNumber);
        pastOrderDetailsTimestamp = view.findViewById(R.id.pastOrderDetailsTimestamp);
        rateOrderPastOrderDetails = view.findViewById(R.id.rateOrderPastOrderDetails);
        totalPricePastOrderDetails = view.findViewById(R.id.totalPricePastOrderDetails);

        reorderButtonPastOrderDetails = view.findViewById(R.id.reorderButtonPastOrderDetails);

        pastOrderDetailsCartItemsRecycler = view.findViewById(R.id.pastOrderDetailsCartItemsRecycler);
        pastOrderDetailsCartItemsAdapter = new PastOrderDetailsCartItemsAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pastOrderDetailsCartItemsRecycler.setLayoutManager(linearLayoutManager);
        pastOrderDetailsCartItemsRecycler.setAdapter(pastOrderDetailsCartItemsAdapter);

        //////////////////////////////////on Activity created///////////////////////////////////////////////
        orderSpecificDetailsViewModel = new ViewModelProvider(this.requireActivity()).get(OrderSpecificDetailsViewModel.class);
        orderSpecificDetailsViewModel.getOrderSpecificDetailsData(getActivity(), pastOrderId).observe(getViewLifecycleOwner(), new Observer<List<OrderSpecificDetailModel>>() {
            @Override
            public void onChanged(List<OrderSpecificDetailModel> orderSpecificDetailModels) {
                orderSpecificDetailModelList = new ArrayList<>(orderSpecificDetailModels);
                addClickSupportToRecycler(pastOrderDetailsCartItemsRecycler, orderSpecificDetailModels);
                setCorrectInfo();
                pastOrderDetailsCartItemsAdapter.setOrderSpecificDetailModels(orderSpecificDetailModels);
            }
        });

        setOnClickListeners();
    }

    private void setOnClickListeners(){
        reorderButtonPastOrderDetails.setOnClickListener(this);
        rateOrderPastOrderDetails.setOnClickListener(this);
        backIconPastOrderDetails.setOnClickListener(this);
    }

    private void setCorrectInfo() {
        pastOrderDetailsOrderNumber.setText("Order #" + orderNumber);
        totalPricePastOrderDetails.setText("Total: $" + totalPrice);
        pastOrderDetailsTimestamp.setText(timestamp);
    }

    private void addClickSupportToRecycler(RecyclerView recycler, List<OrderSpecificDetailModel> orderSpecificDetailModels){
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                navigateToProductPage(orderSpecificDetailModels, position);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
            }
        });
    }

    private void navigateToProductPage(List<OrderSpecificDetailModel> orderSpecificDetailModels, int position) {
        PastOrderDetailsFragmentDirections.ActionPastOrderDetailsFragmentToProductPageFragment action = PastOrderDetailsFragmentDirections.actionPastOrderDetailsFragmentToProductPageFragment();
        action.setMealId(orderSpecificDetailModels.get(position).getMealId());
        navController.navigate(action);
    }

    private void addToCart() {
        firebaseFirestore
                .collection("Users")
                .document(account.getId())
                .collection("Past Orders")
                .document(pastOrderId)
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
                                        .document(account.getId())
                                        .collection("Cart")
                                        .add(cartItem);
                            }
                            openCart();
                        }else{

                        }
                    }
                });
    }

    private void openCart(){
        CartBottomSheetFragment cartBottomSheetFragment = new CartBottomSheetFragment();
        cartBottomSheetFragment.show(getParentFragmentManager(), cartBottomSheetFragment.getTag());
    }

    private void openRateDialog(List<OrderSpecificDetailModel> orderSpecificDetailModelList){
        RatingDialogFragment ratingDialogFragment = new RatingDialogFragment(orderSpecificDetailModelList);
        ratingDialogFragment.show(getParentFragmentManager(), ratingDialogFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.reorderButtonPastOrderDetails:
                addToCart();
                break;
            case R.id.rateOrderPastOrderDetails:
                openRateDialog(orderSpecificDetailModelList);
                break;
            case R.id.backIconPastOrderDetails:
                navController.popBackStack();
                break;
        }
    }
}