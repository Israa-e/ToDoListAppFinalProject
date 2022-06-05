package com.example.todolistapp.Model;

public class ToDoModel extends TaskId {
    private String task, due, category;
    private int status;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    private boolean isFavourite;



    public String getTask() {
        return task;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

}
