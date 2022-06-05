package com.example.todolistapp.StartApp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.Log.Log_in_upActivity;
import com.example.todolistapp.Log.SignUpActivity;
import com.example.todolistapp.R;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_guest;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("logged_user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        if (!user_id.equals("")) {
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setTitle("Lets Get Started");
        _FindId();
        _Listener();
    }

    public void _FindId() {
        btn_login = findViewById(R.id.btn_login);
        btn_guest = findViewById(R.id.btn_guest);
    }

    public void _Listener() {
        btn_login.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, Log_in_upActivity.class);
            startActivity(intent);

            finish();
        });
        btn_guest.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, SignUpActivity.class);

            startActivity(intent);

            finish();
        });


    }



}