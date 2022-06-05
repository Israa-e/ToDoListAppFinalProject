package com.example.todolistapp.Adds;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.R;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView ntask,stask,duetask;
    String name ,status , dueDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        _IdFind();
        GetDataIntent();
        SetDataIntent();
    }

    private void SetDataIntent() {
        ntask.setText(name);
        stask.setText(status);
        duetask.setText(dueDate);
    }

    public void _IdFind() {
        ntask = findViewById(R.id.ntask);
        stask = findViewById(R.id.stask);
        duetask = findViewById(R.id.duetask);
    }
    public void GetDataIntent()
    {
        Intent intent = getIntent();
         name = intent.getStringExtra("task_name");
         status = intent.getStringExtra("task_status");
         dueDate = intent.getStringExtra("task_due_on");
    }

}