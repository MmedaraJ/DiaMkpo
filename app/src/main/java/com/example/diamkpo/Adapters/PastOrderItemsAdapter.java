package com.example.diamkpo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diamkpo.Models.PastOrderModel;
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
import java.text.SimpleDateFormat;
import java.util.List;

public class PastOrderItemsAdapter extends RecyclerView.Adapter<PastOrderItemsAdapter.PastOrderItemsViewHolder> {
    private Context context;
    private List<PastOrderModel> pastOrderModels;
    private PastOrderReorderTvClicked pastOrderReorderTvClicked;
    private int count;

    public PastOrderItemsAdapter(Context context, PastOrderReorderTvClicked pastOrderReorderTvClicked) {
        this.context = context;
        this.pastOrderReorderTvClicked = pastOrderReorderTvClicked;
    }

    public void setPastOrderModels(List<PastOrderModel> pastOrderModels) {
        this.pastOrderModels = pastOrderModels;
        count = 0;
        notifyDataSetChanged();;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @NonNull
    @NotNull
    @Override
    public PastOrderItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_orders_items, parent, false);
        return new PastOrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PastOrderItemsViewHolder holder, int position) {
        holder.bind(pastOrderModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(pastOrderModels == null){
            return 0;
        }
        return pastOrderModels.size();
    }

    class PastOrderItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FirebaseFirestore firebaseFirestore;
        private GoogleSignInAccount account;

        private ConstraintLayout pastOrdersBackground;

        private ImageView pastOrderImage;
        private ImageView pastOrderLikeCircle;

        private TextView pastOrderNumber;
        private TextView numberOfItems;
        private TextView pastOrderPrice;
        private TextView pastOrderDate;
        private TextView pastOrderCompletionStatus;
        private TextView reorderTvPastOrders;

        public PastOrderItemsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            account = GoogleSignIn.getLastSignedInAccount(context);

            pastOrdersBackground = itemView.findViewById(R.id.pastOrdersBackground);

            pastOrderImage = itemView.findViewById(R.id.pastOrderImage);
            pastOrderLikeCircle = itemView.findViewById(R.id.pastOrderLikeCircle);

            pastOrderNumber = itemView.findViewById(R.id.pastOrderNumber);
            numberOfItems = itemView.findViewById(R.id.numberOfItems);
            pastOrderPrice = itemView.findViewById(R.id.pastOrderPrice);
            pastOrderDate = itemView.findViewById(R.id.pastOrderDate);
            pastOrderCompletionStatus = itemView.findViewById(R.id.pastOrderCompletionStatus);
            reorderTvPastOrders = itemView.findViewById(R.id.reorderTvPastOrders);
        }

        public void bind(PastOrderModel pastOrder){
            Glide.with(itemView.getContext())
                    .load(pastOrder.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(pastOrderImage);

            firebaseFirestore
                    .collection("Users")
                    .document(account.getId())
                    .collection("Past Orders")
                    .document(pastOrder.getPastOrderId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null && documentSnapshot.exists()) {
                                    boolean liked = documentSnapshot.getBoolean("liked");
                                    if(liked){
                                        pastOrderLikeCircle.setImageResource(R.drawable.red_heart_shape);
                                    }else{
                                        pastOrderLikeCircle.setImageResource(0);
                                    }
                                }
                            }
                        }
                    });

            pastOrderNumber.setText("#" + pastOrder.getOrderNumber());

            String noOfItems = "";
            if(pastOrder.getNumberOfItems()<2){
                noOfItems = pastOrder.getNumberOfItems() + " item";
            }else{
                noOfItems = pastOrder.getNumberOfItems() + " items";
            }
            numberOfItems.setText(noOfItems);

            pastOrderPrice.setText("$" + round(pastOrder.getTotalPrice(), 2));

            String pattern = "MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(pastOrder.getTimestamp());
            pastOrderDate.setText("" + date);

            //pastOrderCompletionStatus hold for now

            if(pastOrder.getOrderNumber()%2 == 0){
                pastOrdersBackground.setBackgroundResource(R.drawable.green_background);
                reorderTvPastOrders.setBackgroundResource(R.drawable.white_background);
            }else{
                pastOrdersBackground.setBackgroundResource(R.drawable.green_outline);
                reorderTvPastOrders.setBackgroundResource(R.drawable.smaller_green_background);
            }
            count++;

            reorderTvPastOrders.setOnClickListener(this);
        }

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

        @Override
        public void onClick(View v) {
            pastOrderReorderTvClicked.onPastOrderReorderTvClicked(getAdapterPosition());
        }
    }

    public interface PastOrderReorderTvClicked {
        void onPastOrderReorderTvClicked(int position);
    }

}
