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

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.diamkpo.Fragments.CartBottomSheetFragment.round;

public class CheckoutItemsAdapter extends RecyclerView.Adapter<CheckoutItemsAdapter.CheckoutItemsViewHolder> {
    private Context context;
    private List<CartModel> cartModels;
    private CheckoutItemsAdapter.CheckoutItemClicked checkoutItemClicked;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CheckoutItemsAdapter(Context context, CheckoutItemClicked checkoutItemClicked) {
        this.context = context;
        this.checkoutItemClicked = checkoutItemClicked;
    }

    public void setCartModels(List<CartModel> cartModels){
        this.cartModels = cartModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CheckoutItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new CheckoutItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CheckoutItemsViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(cartModels.get(position).getCartId()));
        viewBinderHelper.closeLayout(String.valueOf(cartModels.get(position).getCartId()));
        holder.bind(cartModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(cartModels == null){
            return 0;
        }
        return cartModels.size();
    }

    class CheckoutItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView numberPerMealInCart;
        private TextView nameOfMealInCart;
        private TextView priceOfMealInCart;
        private TextView removeItemTv;

        private SwipeRevealLayout swipeLayout;

        public CheckoutItemsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            numberPerMealInCart = itemView.findViewById(R.id.numberPerMealInCart);
            nameOfMealInCart = itemView.findViewById(R.id.nameOfMealInCart);
            priceOfMealInCart = itemView.findViewById(R.id.priceOfMealInCart);
            removeItemTv = itemView.findViewById(R.id.removeItemTv);
            removeItemTv.setOnClickListener(this);

            swipeLayout = itemView.findViewById(R.id.swipeLayout);
        }

        public void bind(CartModel cartModel){
            numberPerMealInCart.setText(String.valueOf(cartModel.getNumOrders()));
            nameOfMealInCart.setText(cartModel.getNameOfMeal());
            priceOfMealInCart.setText("$" + round(cartModel.getTotalPrice(), 2));
        }

        @Override
        public void onClick(View v) {
            checkoutItemClicked.onRemoveItemClicked(getAdapterPosition());
        }
    }

    public interface CheckoutItemClicked {
        void onRemoveItemClicked(int position);
    }
}
