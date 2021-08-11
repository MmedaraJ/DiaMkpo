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
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LikedMealsAdapter extends RecyclerView.Adapter<LikedMealsAdapter.LikedMealsViewHolder> {
    private Context context;
    private Map<String, Object> likedMealsMap;

    public LikedMealsAdapter(Context context) {
        this.context = context;
    }

    public void setLikedMealsMap(Map<String, Object> likedMealsMap) {
        this.likedMealsMap = likedMealsMap;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public LikedMealsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_meal_item, parent, false);
        return new LikedMealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LikedMealsViewHolder holder, int position) {
        holder.bind(likedMealsMap.keySet().toArray()[position].toString(), likedMealsMap.values().toArray()[position].toString());
    }

    @Override
    public int getItemCount() {
        if(likedMealsMap == null){
            return 0;
        }else{
            return likedMealsMap.size();
        }
    }

    class LikedMealsViewHolder extends RecyclerView.ViewHolder{
        private TextView nameOfLikedMeal;
        private ImageView imageOfLikedMeal;

        public LikedMealsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameOfLikedMeal = itemView.findViewById(R.id.nameOfLikedMeal);
            imageOfLikedMeal = itemView.findViewById(R.id.imageOfLikedMeal);
        }

        public void bind(String name, String image){
            nameOfLikedMeal.setText(name);
            Glide.with(itemView.getContext())
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageOfLikedMeal);
        }
    }
}
