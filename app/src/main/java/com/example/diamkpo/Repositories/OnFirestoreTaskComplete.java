package com.example.diamkpo.Repositories;

import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.Models.PastOrderModel;
import com.example.diamkpo.Models.UserModel;

import java.util.List;

public interface OnFirestoreTaskComplete {
    void mealDataAdded(List<MealModel> mealModelList);
    void highestRatedMealsDataAdded(List<MealModel> highestRatedMealsList);
    void bestMealsDataAdded(List<MealModel> bestDinnerList);
    void categoryDataAdded(List<CategoryModel> categories);
    void cartDataAdded(List<CartModel> cartModels);
    void pastOrderDataAdded(List<PastOrderModel> pastOrderModels);
    void userDataAdded(UserModel userModels);
    void orderDetailsDataAdded(List<OrderSpecificDetailModel> orderSpecificDetailModels);
    void onError(Exception e);
}
