package com.example.diamkpo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diamkpo.Adapters.PastOrderItemsAdapter;
import com.example.diamkpo.Models.PastOrderModel;
import com.example.diamkpo.R;
import com.example.diamkpo.TouchListeners.ItemClickSupport;
import com.example.diamkpo.ViewModels.PastOrderViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PastOrdersFragment extends Fragment implements PastOrderItemsAdapter.PastOrderReorderTvClicked {
    private FirebaseFirestore firebaseFirestore;

    private GoogleSignInAccount account;

    private NavController navController;

    private PastOrderViewModel pastOrderViewModel;

    private TextView pastOrdersFragmentHeaderTv;

    private RecyclerView pastOrdersRecycler;

    private PastOrderItemsAdapter pastOrderItemsAdapter;

    private List<PastOrderModel> allPastOrders;

    private String currentUserId;
    private String pastOrderId;

    public PastOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        currentUserId = "";
        pastOrderId = "";

        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            currentUserId = account.getId();
        }else {
        }

        navController = Navigation.findNavController(view);

        allPastOrders = new ArrayList<>();

        pastOrdersFragmentHeaderTv = view.findViewById(R.id.pastOrdersFragmentHeaderTv);

        //pastOrdersRecycler
        pastOrdersRecycler = view.findViewById(R.id.pastOrdersRecycler);
        pastOrderItemsAdapter = new PastOrderItemsAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pastOrdersRecycler.setLayoutManager(linearLayoutManager);
        pastOrdersRecycler.setAdapter(pastOrderItemsAdapter);

        //////////////////////////////////on Activity created///////////////////////////////////////////////
        pastOrderViewModel = new ViewModelProvider(this.requireActivity()).get(PastOrderViewModel.class);
        pastOrderViewModel.getPastOrderData(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<PastOrderModel>>() {
            @Override
            public void onChanged(List<PastOrderModel> pastOrderModels) {
                allPastOrders = pastOrderModels;
                pastOrderItemsAdapter.setPastOrderModels(pastOrderModels);
            }
        });
        addClickSupportToRecycler();
    }

    /**
     * add click support to recycler
     */
    public void addClickSupportToRecycler(){
        ItemClickSupport.addTo(pastOrdersRecycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                PastOrdersFragmentDirections.ActionPastOrdersFragmentToPastOrderDetailsFragment action = PastOrdersFragmentDirections.actionPastOrdersFragmentToPastOrderDetailsFragment();
                action.setPastOrderId(allPastOrders.get(position).getPastOrderId());
                action.setOrderNumber(allPastOrders.get(position).getOrderNumber());
                action.setTotalPrice(allPastOrders.get(position).getTotalPrice() + "");
                action.setLiked(allPastOrders.get(position).isLiked());
                action.setTimestamp(allPastOrders.get(position).getTimestamp().toString());
                navController.navigate((NavDirections) action);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                pastOrderId = allPastOrders.get(position).getPastOrderId();
                reactToDoubleClick(pastOrderId);
            }
        });
    }

    private void reactToDoubleClick(String pastOrderId){
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Past Orders")
                .document(pastOrderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists()) {
                                boolean liked = documentSnapshot.getBoolean("liked");
                                setNewLike(pastOrderId, !liked);
                            }
                        }
                    }
                });
    }

    private void setNewLike(String pastOrderId, boolean liked){
        HashMap<String, Object> likedMap = new HashMap<>();
        likedMap.put("liked", liked);
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Past Orders")
                .document(pastOrderId)
                .update(likedMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isComplete()){
                            pastOrderItemsAdapter.setCount(0);
                            pastOrderItemsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void addToCart(int position) {
        clearCart();
        firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Past Orders")
                .document(allPastOrders.get(position).getPastOrderId())
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
                                .add(cartItem);
                    }
                    openCart();
                }else{

                }
            }
        });
    }

    private void clearCart() {
        CollectionReference cartRef = firebaseFirestore
                .collection("Users")
                .document(currentUserId)
                .collection("Cart");
        deleteCollection(cartRef, 3);
    }

    private void deleteCollection(CollectionReference collection, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            Task<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<DocumentSnapshot> documents = future.getResult().getDocuments();
            for (DocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteCollection(collection, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }

    private void openCart(){
        CartBottomSheetFragment cartBottomSheetFragment = new CartBottomSheetFragment();
        cartBottomSheetFragment.show(getParentFragmentManager(), cartBottomSheetFragment.getTag());
    }

    @Override
    public void onPastOrderReorderTvClicked(int position) {
        addToCart(position);
    }
}