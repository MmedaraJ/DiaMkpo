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

import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.AccountItemViewHolder> {
    private Context context;
    private Map<String, Integer> accountItems;
    private static final String TAG = "ACCOUNT_ADAPTER";

    public AccountItemAdapter(Context context) {
        this.context = context;
    }

    public void setAccountItems(Map<String, Integer> accountItems) {
        this.accountItems = accountItems;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AccountItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_items, parent, false);
        return new AccountItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccountItemViewHolder holder, int position) {
        holder.bind(accountItems.keySet().toArray()[position].toString() ,(Integer) accountItems.values().toArray()[position]);
    }

    @Override
    public int getItemCount() {
        if(accountItems == null){
            return 0;
        }else{
            return accountItems.size();
        }
    }

    class AccountItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView accountItemImage;

        private TextView accountItemName;

        public AccountItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            accountItemImage = itemView.findViewById(R.id.accountItemImage);
            accountItemName = itemView.findViewById(R.id.accountItemName);
        }

        public void bind(String name, Integer image){
            accountItemName.setText(name);
            accountItemImage.setBackgroundResource(image);
        }
    }
}
