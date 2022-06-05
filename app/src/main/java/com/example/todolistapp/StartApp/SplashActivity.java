package com.example.todolistapp.StartApp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolistapp.R;

public class SplashActivity extends AppCompatActivity {
    private static  int SPLASH_TIMER=5000;

    TextView poweredBy;
    SharedPreferences sharedPreferences;
    ImageView background_image;
    Animation sideAnim,btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        poweredBy = findViewById(R.id.poweredBy);
        background_image =findViewById(R.id.background_image);
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        btnAnim= AnimationUtils.loadAnimation(this,R.anim.btn_anim);
        background_image.setAnimation(sideAnim);
        poweredBy.setAnimation(btnAnim);

        new Handler().postDelayed(() -> {
            sharedPreferences =getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
            boolean isFirstTime =sharedPreferences.getBoolean("firstTime",true);
            if (isFirstTime) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
                Intent i = new Intent(getBaseContext(),OnBoarding.class);
                startActivity(i);
                finish();
            }else {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
                finish();
            }

        },SPLASH_TIMER);
    }
}