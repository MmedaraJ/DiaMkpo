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
import com.example.diamkpo.Repositories.MealRepository;
import com.example.diamkpo.Repositories.OnFirestoreTaskComplete;

import java.util.List;

public class MealViewModel extends ViewModel implements OnFirestoreTaskComplete {
    private MealRepository mealRepository = new MealRepository(this);
    private MutableLiveData<List<MealModel>> mealModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MealModel>> highestRatedMealModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MealModel>> bestMealsMutableLiveData = new MutableLiveData<>();

    public MealViewModel(){
        mealRepository.getMealData();
    }

    public LiveData<List<MealModel>> getMealsModelData() {
        return mealModelMutableLiveData;
    }

    public LiveData<List<MealModel>> getHighestRatedMealsModelData() {
        mealRepository.getHighestRatedMeals();
        return highestRatedMealModelMutableLiveData;
    }

    public LiveData<List<MealModel>> getMealBasedOnTimeOfDay(String category) {
        mealRepository.getBestMealsData(category);
        return bestMealsMutableLiveData;
    }

    @Override
    public void mealDataAdded(List<MealModel> mealModelList) {
        mealModelMutableLiveData.setValue(mealModelList);
    }

    @Override
    public void highestRatedMealsDataAdded(List<MealModel> highestRatedMealsList) {
        highestRatedMealModelMutableLiveData.setValue(highestRatedMealsList);
    }

    @Override
    public void bestMealsDataAdded(List<MealModel> bestDinnerList) {
        bestMealsMutableLiveData.setValue(bestDinnerList);
    }

    @Override
    public void categoryDataAdded(List<CategoryModel> categories) {

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
