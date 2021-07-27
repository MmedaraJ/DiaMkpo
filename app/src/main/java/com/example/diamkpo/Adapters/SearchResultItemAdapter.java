package com.example.diamkpo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchResultItemAdapter extends RecyclerView.Adapter<SearchResultItemAdapter.SearchResultItemViewHolder> {
    private Context context;
    private List<MealModel> mealModels;

    public SearchResultItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SearchResultItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_items, parent, false);
        return new SearchResultItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchResultItemViewHolder holder, int position) {
        holder.bind(mealModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(mealModels == null){
            return 0;
        }
        return mealModels.size();
    }

    public void setMealModels(List<MealModel> mealModels){
        this.mealModels = mealModels;
        notifyDataSetChanged();
    }

    class SearchResultItemViewHolder extends RecyclerView.ViewHolder{
        private FirebaseFirestore firebaseFirestore;
        private GoogleSignInAccount account;

        private ImageView searchResultImage;
        private ImageView searchResultLikeCircle;

        private TextView searchResultProductName;
        private TextView searchResultProductCategory;
        private TextView searchResultPrice;

        public SearchResultItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            account = GoogleSignIn.getLastSignedInAccount(context);

            searchResultImage = itemView.findViewById(R.id.searchResultImage);
            searchResultLikeCircle = itemView.findViewById(R.id.searchResultLikeCircle);

            searchResultProductName = itemView.findViewById(R.id.searchResultProductName);
            searchResultProductCategory = itemView.findViewById(R.id.searchResultProductCategory);
            searchResultPrice = itemView.findViewById(R.id.searchResultPrice);
        }

        public void bind(MealModel meal){
            Glide.with(itemView.getContext())
                    .load(meal.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.burger1)
                    .into(searchResultImage);

            if (account != null) {
                firebaseFirestore
                        .collection("meals")
                        .document(meal.getMealId())
                        .collection("Liked")
                        .document(account.getId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        boolean liked = documentSnapshot.getBoolean("liked");
                                        if (liked) {
                                            searchResultLikeCircle.setImageResource(R.drawable.red_heart_shape);
                                        } else {
                                            searchResultLikeCircle.setImageResource(0);
                                        }
                                    }
                                }
                            }
                        });
            }

            searchResultProductName.setText(meal.getName());
            searchResultProductCategory.setText(meal.getCategory().get(0));
            searchResultPrice.setText("$" + meal.getNormalPrice());
        }
    }
}
