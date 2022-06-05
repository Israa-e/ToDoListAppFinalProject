package com.example.todolistapp.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class UserModel {
    @Exclude
    public String UserModel;

    public <T extends UserModel> T withId(@NonNull final String id) {
        this.UserModel = id;
        return (T) this;
    }
}
