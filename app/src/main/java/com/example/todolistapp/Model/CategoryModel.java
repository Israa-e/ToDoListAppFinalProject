package com.example.todolistapp.Model;

public class CategoryModel extends TaskId{
    String category_name ,id ,uid;
    long time;

    public String getCategory_name() {
        return category_name;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public long getTime() {
        return time;
    }

}
