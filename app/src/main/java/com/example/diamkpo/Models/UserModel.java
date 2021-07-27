package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

public class UserModel {
    @DocumentId
    private String userId;
    private String deliveryAddress;
    private String deliveryProvince;

    private int totalNumberOfOrders;

    public UserModel() {
    }

    public UserModel(String userId, String deliveryAddress, String deliveryProvince, int totalNumberOfOrders) {
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryProvince = deliveryProvince;
        this.totalNumberOfOrders = totalNumberOfOrders;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(String deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }

    public int getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }

    public void setTotalNumberOfOrders(int totalNumberOfOrders) {
        this.totalNumberOfOrders = totalNumberOfOrders;
    }
}