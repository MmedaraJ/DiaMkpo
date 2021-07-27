package com.example.diamkpo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddNewCategoryAdapter extends RecyclerView.Adapter<AddNewCategoryAdapter.AddNewCategoryViewHolder> {
    private Context context;
    private List<String> allNewCategories;
    private NewCategoryItemAdded newCategoryItemAdded;

    public AddNewCategoryAdapter(Context context, NewCategoryItemAdded newCategoryItemAdded) {
        this.context = context;
        this.newCategoryItemAdded = newCategoryItemAdded;
    }

    public void setAllNewCategories(List<String> allNewCategories) {
        this.allNewCategories = allNewCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AddNewCategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_new_category_item, parent, false);
        return new AddNewCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddNewCategoryViewHolder holder, int position) {
        holder.bind(allNewCategories.get(position));
    }

    @Override
    public int getItemCount() {
        if(allNewCategories == null){
            return 0;
        }
        else{
            return allNewCategories.size();
        }
    }

    class AddNewCategoryViewHolder extends RecyclerView.ViewHolder implements EditText.OnFocusChangeListener{
        private EditText newCategoryEditText;

        public AddNewCategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            newCategoryEditText = itemView.findViewById(R.id.newCategoryEditText);
        }

        public void bind(String s){
            newCategoryEditText.setText(s);
            newCategoryEditText.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                newCategoryItemAdded.onItemAdded(getAdapterPosition(), newCategoryEditText.getText().toString());
            }
        }
    }

    public interface NewCategoryItemAdded{
        void onItemAdded(int position, String s);
    }
}
