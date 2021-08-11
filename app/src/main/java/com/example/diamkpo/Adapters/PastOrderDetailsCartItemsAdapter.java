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
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PastOrderDetailsCartItemsAdapter extends RecyclerView.Adapter<PastOrderDetailsCartItemsAdapter.PastOrderDetailsCartItemsViewHolder> {
    private Context context;
    private List<OrderSpecificDetailModel> orderSpecificDetailModels;

    public PastOrderDetailsCartItemsAdapter(Context context) {
        this.context = context;
    }

    public void setOrderSpecificDetailModels(List<OrderSpecificDetailModel> orderSpecificDetailModels){
        this.orderSpecificDetailModels = orderSpecificDetailModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public PastOrderDetailsCartItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_order_details_items, parent, false);
        return new PastOrderDetailsCartItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PastOrderDetailsCartItemsViewHolder holder, int position) {
        holder.bind(orderSpecificDetailModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(orderSpecificDetailModels == null){
            return 0;
        }
        return orderSpecificDetailModels.size();
    }

    class PastOrderDetailsCartItemsViewHolder extends RecyclerView.ViewHolder{
        private TextView numberPerMealInPastOrderDetails;
        private TextView nameOfMealInPastOrderDetails;

        private ImageView mealImagePastOrderDetails;

        public PastOrderDetailsCartItemsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            numberPerMealInPastOrderDetails = itemView.findViewById(R.id.numberPerMealInPastOrderDetails);
            nameOfMealInPastOrderDetails = itemView.findViewById(R.id.nameOfLikedMeal);
            mealImagePastOrderDetails = itemView.findViewById(R.id.mealImagePastOrderDetails);
        }

        public void bind(OrderSpecificDetailModel orderSpecificDetail){
            numberPerMealInPastOrderDetails.setText(String.valueOf(orderSpecificDetail.getNumOrders()));
            nameOfMealInPastOrderDetails.setText(orderSpecificDetail.getNameOfMeal());
            Glide.with(itemView.getContext())
                    .load(orderSpecificDetail.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(mealImagePastOrderDetails);
        }
    }
}
