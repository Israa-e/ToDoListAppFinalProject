package com.example.todolistapp.Model;

public class ImageModel {
    private String imageUri;

    public ImageModel(String imageUri) {
        this.imageUri = imageUri;
    }

    public ImageModel() {
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
