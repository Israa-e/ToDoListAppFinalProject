package com.example.todolistapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todolistapp.Adds.AddNewTask;
import com.example.todolistapp.DrawerThings.FavoritesActivity;
import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.example.todolistapp.Adds.TaskDetailsActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private final List<ToDoModel> toDoModelList;
    private final HomePageActivity activity;
    private final Activity act;
    private FirebaseFirestore firebaseFirestore;

    public ToDoAdapter(HomePageActivity homePageActivity,Activity act ,List<ToDoModel> toDoModelList) {
        this.toDoModelList = toDoModelList;
        this.activity = homePageActivity;
        this.act=act;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    public void deleteTask(int position) {
        ToDoModel toDoModel = toDoModelList.get(position);
        firebaseFirestore.collection("task").document(toDoModel.TaskId).delete();
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position) {
        ToDoModel toDoModel = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("uid", toDoModel.TaskId);
        bundle.putInt("status", toDoModel.getStatus());
        bundle.putBoolean("fav", toDoModel.isFavourite());
        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
        notifyItemChanged(position);
    }

    public Context getContext() {
        return activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = toDoModelList.get(position);
        holder.text_task.setText(toDoModel.getTask());
        holder.dueDate.setText("Due On" + toDoModel.getDue());
        holder.checkBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(activity, TaskDetailsActivity.class);
            intent.putExtra("task_id",toDoModel.TaskId);
            intent.putExtra("task_name",toDoModel.getTask());
            intent.putExtra("task_due_on",toDoModel.getDue());
            intent.putExtra("task_status",toDoModel.getStatus()+"");
            intent.putExtra("task_favourite",toDoModel.isFavourite());
            activity.startActivity(intent);
        });
        holder.isFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toDoModel.isFavourite()){
                    holder.isFavourite.setImageResource(R.drawable.ic_baseline_star_24);
                    Intent intent = new Intent(getContext(), FavoritesActivity.class);
                    intent.putExtra("Favorites",toDoModel.isFavourite());
                    toDoModel.setFavourite(true);
                }else {
                    holder.isFavourite.setImageResource(R.drawable.ic_baseline_star_border_24);
                    toDoModel.setFavourite(false);
                }
                firebaseFirestore.collection("task").document(toDoModel.TaskId).update("fav", toDoModel.isFavourite()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Task Is Favourite", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                }
        });
        if(toDoModel.getStatus()==1){
            holder.itemView.setAlpha(0.4f);
            holder.text_task.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.text_task.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked() ) {
                firebaseFirestore.collection("task").document(toDoModel.TaskId).update("status", 1).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Task Completed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "Task Uncompleted", Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("task").document(toDoModel.TaskId).update("status", 0);
            }
        });


    }


    private boolean toBoolean(int Status) {
        return Status != 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dueDate;
        TextView text_task;
        CheckBox checkBox;
        ImageButton isFavourite;


        public MyViewHolder(@NonNull View v) {
            super(v);
            dueDate = v.findViewById(R.id.due_date_tv);
            text_task= v.findViewById(R.id.text_task);
            checkBox = v.findViewById(R.id.mCheckBox);
            isFavourite=v.findViewById(R.id.starBtn);
        }
    }


}
