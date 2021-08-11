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

public class MenuCategoryItemAdapter extends RecyclerView.Adapter<MenuCategoryItemAdapter.MenuCategoryItemViewHolder>{
    private Context context;
    private List<MealModel> mealModels;

    public MenuCategoryItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MenuCategoryItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_category_item_items, parent, false);
        return new MenuCategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MenuCategoryItemViewHolder holder, int position) {
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

    class MenuCategoryItemViewHolder extends RecyclerView.ViewHolder{
        private FirebaseFirestore firebaseFirestore;
        private GoogleSignInAccount account;

        private TextView menuItemItemNameTv;
        private TextView menuItemDescription;
        private TextView menuItemPrice;

        private ImageView menuItemLikeCircle;
        private ImageView menuItemImageView;

        public MenuCategoryItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            account = GoogleSignIn.getLastSignedInAccount(context);

            menuItemItemNameTv = itemView.findViewById(R.id.menuItemItemNameTv);
            menuItemDescription = itemView.findViewById(R.id.menuItemDescription);
            menuItemPrice = itemView.findViewById(R.id.menuItemPrice);

            menuItemLikeCircle = itemView.findViewById(R.id.menuItemLikeCircle);
            menuItemImageView = itemView.findViewById(R.id.menuItemImageView);
        }

        public void bind(MealModel meal){
            Glide.with(itemView.getContext())
                    .load(meal.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(menuItemImageView);

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
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot != null && documentSnapshot.exists()) {
                                        boolean liked = documentSnapshot.getBoolean("liked");
                                        if(liked){
                                            menuItemLikeCircle.setImageResource(R.drawable.red_heart_shape);
                                        }else{
                                            menuItemLikeCircle.setImageResource(0);
                                        }
                                    }
                                }
                            }
                        });
            }

            menuItemItemNameTv.setText(meal.getName());
            menuItemDescription.setText(meal.getDescription());
            menuItemPrice.setText("$" + meal.getNormalPrice());
        }
    }
}
