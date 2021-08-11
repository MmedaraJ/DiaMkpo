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
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchCategoryItemsAdapter extends RecyclerView.Adapter<SearchCategoryItemsAdapter.SearchCategoryItemsViewModel> {
    private Context context;
    private List<CategoryModel> categoryModels;

    public SearchCategoryItemsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SearchCategoryItemsViewModel onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_items, parent, false);
        return new SearchCategoryItemsViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchCategoryItemsViewModel holder, int position) {
        holder.bind(categoryModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(categoryModels == null){
            return 0;
        }else{
            return categoryModels.size();
        }
    }

    public void setCategoryModels(List<CategoryModel> categoryModels){
        this.categoryModels = categoryModels;
        notifyDataSetChanged();
    }

    class SearchCategoryItemsViewModel extends RecyclerView.ViewHolder {
        private TextView categoryNameSearchFragment;
        private ImageView categoryImage;

        public SearchCategoryItemsViewModel(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryNameSearchFragment = itemView.findViewById(R.id.categoryNameSearchFragment);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }

        public void bind(CategoryModel category){
            categoryNameSearchFragment.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(categoryImage);
        }
    }
}
