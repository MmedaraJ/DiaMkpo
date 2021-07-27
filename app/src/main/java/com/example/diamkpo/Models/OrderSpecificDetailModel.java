package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

public class OrderSpecificDetailModel {
    @DocumentId
    private String orderSpecificDetailId;
    private String image;
    private String nameOfMeal;
    private String mealId;

    private Double totalPrice;

    private int numOrders;

    private boolean liked;

    public OrderSpecificDetailModel() {
    }

    public OrderSpecificDetailModel(String orderSpecificDetailId, String image, String nameOfMeal, String mealId, Double totalPrice, int numOrders, boolean liked) {
        this.orderSpecificDetailId = orderSpecificDetailId;
        this.image = image;
        this.nameOfMeal = nameOfMeal;
        this.mealId = mealId;
        this.totalPrice = totalPrice;
        this.numOrders = numOrders;
        this.liked = liked;
    }

    public String getOrderSpecificDetailId() {
        return orderSpecificDetailId;
    }

    public void setOrderSpecificDetailId(String orderSpecificDetailId) {
        this.orderSpecificDetailId = orderSpecificDetailId;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNameOfMeal() {
        return nameOfMeal;
    }

    public void setNameOfMeal(String nameOfMeal) {
        this.nameOfMeal = nameOfMeal;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
