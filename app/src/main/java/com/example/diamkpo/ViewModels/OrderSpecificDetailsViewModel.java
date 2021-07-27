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
import com.example.diamkpo.Repositories.OrderSpecificDetailsRepository;

import java.util.List;

public class OrderSpecificDetailsViewModel extends ViewModel implements OnFirestoreTaskComplete {
    private OrderSpecificDetailsRepository orderSpecificDetailsRepository;
    private MutableLiveData<List<OrderSpecificDetailModel>> orderSpecificDetailsMutableLiveData;

    public OrderSpecificDetailsViewModel() {
        orderSpecificDetailsRepository = new OrderSpecificDetailsRepository(this);
        orderSpecificDetailsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<OrderSpecificDetailModel>> getOrderSpecificDetailsData(Context context, String pastOrderId) {
        orderSpecificDetailsRepository.getOrderSpecificDetails(context, pastOrderId);
        return orderSpecificDetailsMutableLiveData;
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

    }

    @Override
    public void orderDetailsDataAdded(List<OrderSpecificDetailModel> orderSpecificDetailModels) {
        orderSpecificDetailsMutableLiveData.setValue(orderSpecificDetailModels);
    }

    @Override
    public void onError(Exception e) {

    }
}
