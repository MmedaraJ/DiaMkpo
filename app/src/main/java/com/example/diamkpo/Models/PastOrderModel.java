package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class PastOrderModel implements Serializable {
    @DocumentId
    private String pastOrderId;
    private String image;

    private Double totalPrice;

    private int numberOfItems;
    private int orderNumber;

    private boolean liked;

    private Date timestamp;

    public PastOrderModel() {
    }

    public PastOrderModel(String pastOrderId, String image, Double totalPrice
            , int numberOfItems, int orderNumber, boolean liked, Date timestamp) {
        this.pastOrderId = pastOrderId;
        this.image = image;
        this.totalPrice = totalPrice;
        this.numberOfItems = numberOfItems;
        this.orderNumber = orderNumber;
        this.liked = liked;
        this.timestamp = timestamp;
    }

    public String getPastOrderId() {
        return pastOrderId;
    }

    public void setPastOrderId(String pastOrderId) {
        this.pastOrderId = pastOrderId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
