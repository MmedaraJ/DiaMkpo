package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class MealModel {
    @DocumentId
    private String mealId;
    private String name;
    private String image;
    private String description;
    private String visibility;

    private double avgRating;
    private int numOrders;
    private double dealPrice;
    private double normalPrice;
    private int numRatings;

    private ArrayList<String> category;

    public MealModel() {
    }

    public MealModel(String mealId, String name, String image, String description,
                     String visibility, double avgRating, int numOrders, double dealPrice,
                     double normalPrice, int numRatings, ArrayList<String> category) {
        this.mealId = mealId;
        this.name = name;
        this.image = image;
        this.description = description;
        this.visibility = visibility;
        this.avgRating = avgRating;
        this.numOrders = numOrders;
        this.dealPrice = dealPrice;
        this.normalPrice = normalPrice;
        this.numRatings = numRatings;
        this.category = category;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }
}
