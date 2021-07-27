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

public class OurCustomersFavouritesAdapter extends RecyclerView.Adapter<OurCustomersFavouritesAdapter.OurCustomersFavouritesViewHolder> {
    private List<MealModel> mealModels;
    private Context context;
    private static final String TAG = "CUST_FAV_ADAPTER";

    public OurCustomersFavouritesAdapter(Context context) {
        this.context = context;
    }

    public void setMealModels(List<MealModel> mealModels){
        this.mealModels = mealModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public OurCustomersFavouritesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_meals_items, parent, false);
        return new OurCustomersFavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OurCustomersFavouritesViewHolder holder, int position) {
        holder.bind(mealModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(mealModels == null){
            return 0;
        }else{
            return mealModels.size();
        }
    }

    class OurCustomersFavouritesViewHolder extends RecyclerView.ViewHolder{
        private FirebaseFirestore firebaseFirestore;
        private GoogleSignInAccount account;

        private ImageView favouritesImage;
        private ImageView favouritesLikeCircle;

        private TextView favouriteProductName;
        private TextView favouriteProductCategory;
        private TextView favouritesPrice;

        public OurCustomersFavouritesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            account = GoogleSignIn.getLastSignedInAccount(context);

            favouritesImage = itemView.findViewById(R.id.favouritesImage);
            favouritesLikeCircle = itemView.findViewById(R.id.favouritesLikeCircle);

            favouriteProductName = itemView.findViewById(R.id.favouriteProductName);
            favouriteProductCategory = itemView.findViewById(R.id.favouriteProductCategory);
            favouritesPrice = itemView.findViewById(R.id.favouritesPrice);
        }

        public void bind(MealModel meal){
            Glide.with(itemView.getContext())
                    .load(meal.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.burger1)
                    .into(favouritesImage);
            //like
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
                                        favouritesLikeCircle.setImageResource(R.drawable.red_heart_shape);
                                    }else{
                                        favouritesLikeCircle.setImageResource(0);
                                    }
                                }
                            }
                        }
                    });

            favouriteProductName.setText(meal.getName());
            favouriteProductCategory.setText(meal.getCategory().get(0));
            favouritesPrice.setText("$" + meal.getNormalPrice());
        }
    }
}
