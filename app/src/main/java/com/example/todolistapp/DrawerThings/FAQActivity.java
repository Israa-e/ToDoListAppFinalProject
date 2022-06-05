package com.example.todolistapp.DrawerThings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolistapp.R;

public class FAQActivity extends AppCompatActivity {
    ImageView notification_issues,
            sound_issues,
            pro_issues,
            format_issues,
            calendar_issues,
            account_issues;
    TextView not_dec,
            sound_dec,
            calendar_doc,
            format_dec,
            pro_dec,
            account_dec;
    boolean isOpenNot = false;
    boolean isOpenS = false;
    boolean isOpenP = false;
    boolean isOpenF = false;
    boolean isOpenC = false;
    boolean isOpenA = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqactivity);
        _FindId();
        setTitle("FAQ");
        _Listener();
    }

    public void _FindId() {
        //ImageViews ID
        notification_issues = findViewById(R.id.notification_issues);
        sound_issues = findViewById(R.id.sound_issues);
        pro_issues = findViewById(R.id.pro_issues);
        format_issues = findViewById(R.id.format_issues);
        calendar_issues = findViewById(R.id.calendar_issues);
        account_issues = findViewById(R.id.account_issues);
        //TextView ID

        not_dec = findViewById(R.id.not_dec);
        sound_dec = findViewById(R.id.sound_dec);
        pro_dec = findViewById(R.id.pro_dec);
        format_dec = findViewById(R.id.format_dec);
        calendar_doc = findViewById(R.id.calendar_dec);
        account_dec = findViewById(R.id.account_dec);

    }


    public void _Listener() {

        notification_issues.setOnClickListener(v -> {
            if (isOpenNot == false) {
                notification_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                not_dec.setVisibility(View.VISIBLE);
                isOpenNot = true;
            } else {
                notification_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                not_dec.setVisibility(View.GONE);
                isOpenNot = false;
            }
        });

        sound_issues.setOnClickListener(v -> {
            if (isOpenS == false) {
                sound_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                sound_dec.setVisibility(View.VISIBLE);
                isOpenS = true;
            } else {
                sound_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                sound_dec.setVisibility(View.GONE);
                isOpenS = false;
            }
        });

        pro_issues.setOnClickListener(v -> {
            if (isOpenP == false) {
                pro_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                pro_dec.setVisibility(View.VISIBLE);
                isOpenP= true;
            } else {
                pro_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                pro_dec.setVisibility(View.GONE);
                isOpenP = false;
            }
        });

        format_issues.setOnClickListener(v -> {
            if (isOpenF == false) {
                format_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                format_dec.setVisibility(View.VISIBLE);
                isOpenF = true;
            } else {
                format_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                format_dec.setVisibility(View.GONE);
                isOpenF = false;
            }
        });

        calendar_issues.setOnClickListener(v -> {
            if (isOpenC == false) {
                calendar_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                calendar_doc.setVisibility(View.VISIBLE);
                isOpenC = true;
            } else {
                calendar_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                calendar_doc.setVisibility(View.GONE);
                isOpenC = false;
            }
        });

        account_issues.setOnClickListener(v -> {
            if (isOpenA == false) {
                account_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                account_dec.setVisibility(View.VISIBLE);
                isOpenA = true;
            } else {
                account_issues.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                account_dec.setVisibility(View.GONE);
                isOpenA = false;
            }
        });

    }
}