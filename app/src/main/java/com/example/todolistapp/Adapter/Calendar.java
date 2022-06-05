package com.example.todolistapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class Calendar extends RecyclerView.Adapter<Calendar.ViewHolder> {

    private final List<ToDoModel> toDoModelList;
    private final HomePageActivity activity;
    private final Activity act;
    private FirebaseFirestore firebaseFirestore;

    public Calendar(HomePageActivity homePageActivity,Activity act ,List<ToDoModel> toDoModelList) {
        this.toDoModelList = toDoModelList;
        this.activity = homePageActivity;
        this.act=act;
    }

    @NonNull
    @Override
    public Calendar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_calendar, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new Calendar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Calendar.ViewHolder holder, int position) {

        ToDoModel toDoModel = toDoModelList.get(position);
        holder.tv_task.setText(toDoModel.getTask());

    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }
    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
          TextView tv_task;

        public ViewHolder(@NonNull View v) {
            super(v);
           tv_task=v.findViewById(R.id.tv_task);
        }
    }
}
