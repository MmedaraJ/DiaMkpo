package com.example.diamkpo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RateItemsAdapter extends RecyclerView.Adapter<RateItemsAdapter.RateItemsViewHolder> {
    private Context context;
    private List<OrderSpecificDetailModel> orderSpecificDetailModels;
    private NewRatingAdded newRatingAdded;

    public RateItemsAdapter(Context context, NewRatingAdded newRatingAdded) {
        this.context = context;
        this.newRatingAdded = newRatingAdded;
    }

    public void setOrderSpecificDetailModels(List<OrderSpecificDetailModel> orderSpecificDetailModels) {
        this.orderSpecificDetailModels = orderSpecificDetailModels;
    }

    @NonNull
    @NotNull
    @Override
    public RateItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rate_items_item, parent, false);
        return new RateItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RateItemsViewHolder holder, int position) {
        holder.bind(orderSpecificDetailModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(orderSpecificDetailModels == null){
            return 0;
        }else{
            return orderSpecificDetailModels.size();
        }
    }

    class RateItemsViewHolder extends RecyclerView.ViewHolder implements EditText.OnFocusChangeListener,
            RatingBar.OnRatingBarChangeListener {
        private TextView mealToBeRated;
        private RatingBar ratingBar;
        private EditText ratingText;

        public RateItemsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mealToBeRated = itemView.findViewById(R.id.mealToBeRated);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingText = itemView.findViewById(R.id.ratingText);
        }

        public void bind(OrderSpecificDetailModel orderSpecificDetail){
            mealToBeRated.setText(orderSpecificDetail.getNameOfMeal());
            ratingBar.setOnRatingBarChangeListener(this);
            ratingText.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getId() == R.id.ratingText){
                if(!hasFocus){
                    newRatingAdded.onTextRatingAdded(getAdapterPosition(), ratingText.getText().toString());
                }
            }
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if(ratingBar.getId() == R.id.ratingBar) {
                if(fromUser)
                newRatingAdded.onBarRatingAdded(getAdapterPosition(), this.ratingBar.getRating());
            }
        }
    }

    public interface NewRatingAdded{
        void onBarRatingAdded(int position, float f);
        void onTextRatingAdded(int position, String s);
    }
}
