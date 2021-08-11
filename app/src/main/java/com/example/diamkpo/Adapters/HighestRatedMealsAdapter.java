package com.example.diamkpo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HighestRatedMealsAdapter extends RecyclerView.Adapter<HighestRatedMealsAdapter.HighestRatedMealsViewHolder> {
    private List<MealModel> mealList;
    private Context context;
    private static final String TAG = "HIGHEST RATED ADAPTER";

    public HighestRatedMealsAdapter(Context context) {
        this.context = context;
    }

    public void setMealsList(List<MealModel> mealModel) {
        this.mealList = mealModel;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public HighestRatedMealsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.highest_rated_meals_items, parent, false);
        return new HighestRatedMealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HighestRatedMealsViewHolder holder, int position) {
        holder.bind(mealList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mealList ==null){
            return 0;
        }else{
            return mealList.size();
        }
    }

    class HighestRatedMealsViewHolder extends RecyclerView.ViewHolder {
        private FirebaseFirestore firebaseFirestore;
        private GoogleSignInAccount account;

        private ImageView highestRatedProductImage;
        private ImageView highestRatingLikeCircle;

        private TextView highestRatingProductName;
        private TextView highestRatingRating;

        public HighestRatedMealsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            account = GoogleSignIn.getLastSignedInAccount(context);

            highestRatedProductImage = itemView.findViewById(R.id.highestRatedProductImage);
            highestRatingLikeCircle = itemView.findViewById(R.id.highestRatingLikeCircle);
            highestRatingProductName = itemView.findViewById(R.id.highestRatingProductName);
            highestRatingRating = itemView.findViewById(R.id.highestRatingRating);
        }

        public void bind(MealModel meal){
            Glide.with(itemView.getContext())
                    .load(meal.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(highestRatedProductImage);

            firebaseFirestore
                    .collection("meals")
                    .document(meal.getMealId())
                    .collection("Liked")
                    .document(account.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null && documentSnapshot.exists()) {
                                    boolean liked = documentSnapshot.getBoolean("liked");
                                    if(liked){
                                        highestRatingLikeCircle.setImageResource(R.drawable.red_heart_shape);
                                    }else{
                                        highestRatingLikeCircle.setImageResource(0);
                                    }
                                }
                            }
                        }
                    });

            highestRatingProductName.setText(meal.getName());
            highestRatingRating.setText(String.valueOf(round(meal.getAvgRating(), 2)));
        }

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
