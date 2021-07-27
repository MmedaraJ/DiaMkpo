package com.example.diamkpo.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diamkpo.Models.CartModel;
import com.example.diamkpo.Models.CategoryModel;
import com.example.diamkpo.Models.MealModel;
import com.example.diamkpo.Models.OrderSpecificDetailModel;
import com.example.diamkpo.Models.PastOrderModel;
import com.example.diamkpo.Models.UserModel;
import com.example.diamkpo.Repositories.OnFirestoreTaskComplete;
import com.example.diamkpo.Repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel implements OnFirestoreTaskComplete {
    private UserRepository userRepository = new UserRepository(this);
    private MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();

    public UserViewModel() {
    }

    public LiveData<UserModel> getUserModelData(Context context) {
        userRepository.getUserData(context);
        return userModelMutableLiveData;
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

    }

    @Override
    public void cartDataAdded(List<CartModel> cartModels) {

    }

    @Override
    public void pastOrderDataAdded(List<PastOrderModel> pastOrderModels) {

    }

    @Override
    public void userDataAdded(UserModel userModels) {
        userModelMutableLiveData.setValue(userModels);
    }

    @Override
    public void orderDetailsDataAdded(List<OrderSpecificDetailModel> orderSpecificDetailModels) {

    }

    @Override
    public void onError(Exception e) {

    }
}
