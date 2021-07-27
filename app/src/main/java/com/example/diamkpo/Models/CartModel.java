package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

public class CartModel {
    @DocumentId
    private String cartId;
    private String nameOfMeal;
    private String mealId;
    private String image;

    private Double totalPrice;

    private int numOrders;

    private boolean liked;

    public CartModel() {
    }

    public CartModel(String cartId, String nameOfMeal, String mealId, String image, Double totalPrice, int numOrders, boolean liked) {
        this.cartId = cartId;
        this.nameOfMeal = nameOfMeal;
        this.mealId = mealId;
        this.image = image;
        this.totalPrice = totalPrice;
        this.numOrders = numOrders;
        this.liked = liked;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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