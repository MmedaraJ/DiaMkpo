package com.example.diamkpo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuCategoriesAdapter extends RecyclerView.Adapter<MenuCategoriesAdapter.MenuCategoriesViewHolder> {
    private List<CategoryModel> categories;
    private MenuCategoryItemClicked menuCategoryItemClicked;
    private Context context;
    private int checkedPosition;
    private static final String TAG = "MENU_CATEGORY_ITEM";

    public MenuCategoriesAdapter(Context context, MenuCategoryItemClicked menuCategoryItemClicked) {
        this.context = context;
        this.menuCategoryItemClicked = menuCategoryItemClicked;
        checkedPosition = 0;
    }

    @NonNull
    @NotNull
    @Override
    public MenuCategoriesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_categories_item, parent, false);
        return new MenuCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MenuCategoriesViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        if(categories == null){
            return 0;
        }else{
            return categories.size();
        }
    }

    public void setCategories(List<CategoryModel> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    public CategoryModel getSelected(){
        if(checkedPosition != -1){
            return categories.get(checkedPosition);
        }
        return null;
    }

    public void setSelected(int checkedPosition){
        this.checkedPosition = checkedPosition;
    }

    public int getCheckedPosition(){
        return checkedPosition;
    }

    class MenuCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView menuItemNameTv;

        public MenuCategoriesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            menuItemNameTv = itemView.findViewById(R.id.menuItemNameTv);
        }

        public void bind(CategoryModel category){
            if(checkedPosition == -1){
                menuItemNameTv.setBackgroundResource(0);
            }
            else{
                if(checkedPosition == getAdapterPosition()){
                    menuItemNameTv.setBackgroundResource(R.drawable.smaller_green_background);
                }
                else{
                    menuItemNameTv.setBackgroundResource(0);
                }
            }
            menuItemNameTv.setText(category.getName());
            menuItemNameTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            menuItemNameTv.setBackgroundResource(R.drawable.smaller_green_background);
            if(checkedPosition != getAdapterPosition()){
                Log.d(TAG, "---------------------------------------- checked position is " + checkedPosition);
                notifyItemChanged(checkedPosition);
                checkedPosition = getAdapterPosition();
            }
            menuCategoryItemClicked.onItemClicked(checkedPosition);
        }
    }

    public interface MenuCategoryItemClicked {
        void onItemClicked(int position);
    }
}
