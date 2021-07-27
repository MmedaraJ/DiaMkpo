package com.example.diamkpo.Models;

import com.google.firebase.firestore.DocumentId;

public class CategoryModel {
    @DocumentId
    private String categoryId;
    private String name;
    private String image;

    private int priority;

    public CategoryModel() {
    }

    public CategoryModel(String categoryId, String name, String image, int priority) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.priority = priority;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
