package com.example.diamkpo.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.Models.PastOrderModel;
import com.example.diamkpo.Models.UserModel;
import com.example.diamkpo.Repositories.CategoryRepository;
import com.example.diamkpo.Repositories.OnFirestoreTaskComplete;

import java.util.List;

public class CategoryViewModel extends ViewModel implements OnFirestoreTaskComplete {
    private CategoryRepository categoryRepository = new CategoryRepository(this);
    private MutableLiveData<List<CategoryModel>> categoryMutableLiveData = new MutableLiveData<>();

    public CategoryViewModel() {
        categoryRepository.getCategoryData();
    }

    public LiveData<List<CategoryModel>> getCategoryData(){
        return categoryMutableLiveData;
    }

    @Override
    public void mealDataAdded(List<MealModel> mealModelList) {

    }

    @Override
    public void highestRatedMealsDataAdded(List<MealModel> highestRatedMealsList) {

    }

    @Override
    public void bestMealsDataAdded(List<MealModel> bestDinnerList) {

    }

    @Override
    public void categoryDataAdded(List<CategoryModel> categories) {
        categoryMutableLiveData.setValue(categories);
    }

    @Override
    public void cartDataAdded(List<CartModel> cartModels) {

    }

    @Override
    public void pastOrderDataAdded(List<PastOrderModel> pastOrderModels) {

    }

    @Override
    public void userDataAdded(UserModel userModels) {

    }

    @Override
    public void orderDetailsDataAdded(List<OrderSpecificDetailModel> orderSpecificDetailModels) {

    }

    @Override
    public void onError(Exception e) {

    }
}
