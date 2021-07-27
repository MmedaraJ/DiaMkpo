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
import com.example.diamkpo.Repositories.CartRepository;
import com.example.diamkpo.Repositories.OnFirestoreTaskComplete;

import java.util.List;

public class CartViewModel extends ViewModel implements OnFirestoreTaskComplete {
    private CartRepository cartRepository;
    private MutableLiveData<List<CartModel>> cartMutableLiveData;

    public CartViewModel() {
        cartRepository = new CartRepository(this);
        cartMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<CartModel>> getCartData(Context context){
        cartRepository.getCartData(context);
        return cartMutableLiveData;
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
        cartMutableLiveData.setValue(cartModels);
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
