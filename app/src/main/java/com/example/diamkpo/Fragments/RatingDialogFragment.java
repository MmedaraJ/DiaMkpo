package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.diamkpo.Adapters.RateItemsAdapter;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingDialogFragment extends DialogFragment implements View.OnClickListener, RateItemsAdapter.NewRatingAdded {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private RecyclerView rateItemsRecycler;

    private RateItemsAdapter rateItemsAdapter;

    private Button submitRatingButton;

    private List<OrderSpecificDetailModel> orderSpecificDetailModelList;

    private String currentUserId;

    private List<Float> barRatings;
    private List<String> textRatings;

    private static final String TAG = "RATINGSSSS";

    public RatingDialogFragment() {
        // Required empty public constructor
    }

    public RatingDialogFragment(List<OrderSpecificDetailModel> orderSpecificDetailModelList){
        this.orderSpecificDetailModelList = orderSpecificDetailModelList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        if(account != null){
            currentUserId = account.getId();
        }

        submitRatingButton = view.findViewById(R.id.submitRatingButton);
        submitRatingButton.setOnClickListener(this);

        rateItemsRecycler = view.findViewById(R.id.rateItemsRecycler);
        rateItemsAdapter = new RateItemsAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager0 = new LinearLayoutManager(getActivity());
        linearLayoutManager0.setOrientation(LinearLayoutManager.VERTICAL);
        rateItemsRecycler.setLayoutManager(linearLayoutManager0);
        rateItemsRecycler.setAdapter(rateItemsAdapter);
        rateItemsAdapter.setOrderSpecificDetailModels(orderSpecificDetailModelList);

        barRatings = new ArrayList<>();
        //barRatings.add(-1.0F);
        textRatings = new ArrayList<>();
        //textRatings.add("");
    }

    private void submitRatings() {
        for(int i=0; i< barRatings.size(); i++) {
            Map<String, Object> ratingsMap = new HashMap<>();
            ratingsMap.put("rating", barRatings.get(i));
            ratingsMap.put("text", textRatings.get(i));
            ratingsMap.put("timestamp", new Date());

            firebaseFirestore
                    .collection("meals")
                    .document(orderSpecificDetailModelList.get(i).getMealId())
                    .collection("rating")
                    .document(currentUserId)
                    .set(ratingsMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBarRatingAdded(int position, float f) {
        if(f>-1) if(position>-1) {
            if(barRatings.size()-1>=position) barRatings.set(position, f);
            else barRatings.add(f);
        }
    }

    @Override
    public void onTextRatingAdded(int position, String s) {
        if(!s.isEmpty()) if(position>-1) {
            if(textRatings.size()-1>=position) textRatings.set(position, s);
            else textRatings.add(s);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitRatingButton){
            submitRatings();
        }
    }
}