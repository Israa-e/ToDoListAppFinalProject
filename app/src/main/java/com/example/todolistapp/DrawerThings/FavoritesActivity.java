package com.example.todolistapp.DrawerThings;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolistapp.Adapter.ToDoAdapter;
import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView empty_imageView;
    TextView no_data;
    private List<ToDoModel> toDoModels;
    private ListenerRegistration listenerRegistration;
    private ToDoAdapter toDoAdapter;
    private com.example.todolistapp.HomePageActivity HomePageActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.fav_rec);
        empty_imageView=findViewById(R.id.empty_imageView_task_fav);
        no_data=findViewById(R.id.no_task_fav);
        setTitle("Favorites");
        Intent intent = getIntent();
        toDoModels = (List<ToDoModel>) intent.getSerializableExtra("fav");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toDoModels = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(HomePageActivity,getParent(), toDoModels);
        if(toDoModels.size()==0){
            empty_imageView.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else {
            empty_imageView.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(toDoAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();
                Log.d("TAG", "onScrolled: " + firstElementPosition);

                if (firstElementPosition < 0) {
                    empty_imageView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    empty_imageView.setVisibility(View.GONE);
                    no_data.setVisibility(View.GONE);
                }
            }

    });
}
}