package com.example.diamkpo.Adapters;

import android.content.Context;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder> {
    private Context context;
    private List<CartModel> cartModels;
    private CartItemClicked cartItemClicked;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CartItemsAdapter(Context context, CartItemClicked cartItemClicked) {
        this.context = context;
        this.cartItemClicked = cartItemClicked;
    }

    public void setCartModels(List<CartModel> cartModels){
        this.cartModels = cartModels;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new CartItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartItemsViewHolder holder, int position) {
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

    class CartItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView numberPerMealInCart;
        private TextView nameOfMealInCart;
        private TextView priceOfMealInCart;
        private TextView removeItemTv;

        private SwipeRevealLayout swipeLayout;

        public CartItemsViewHolder(@NonNull @NotNull View itemView) {
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

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();
            BigDecimal bd = BigDecimal.valueOf(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

        @Override
        public void onClick(View v) {
            cartItemClicked.onRemoveItemClicked(getAdapterPosition());
        }
    }

    public interface CartItemClicked {
        void onRemoveItemClicked(int position);
    }
}
