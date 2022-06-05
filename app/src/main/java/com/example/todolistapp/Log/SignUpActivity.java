package com.example.todolistapp.Log;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextInputEditText fullName, phone, email, passwordSingUp, passwordSingUp2;
    AppCompatButton login;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        _FindId();
        _Listener();

    }

    public void _FindId() {
        fullName = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        passwordSingUp = findViewById(R.id.passwordSingUp);
        passwordSingUp2 = findViewById(R.id.passwordSingUp2);
        login = findViewById(R.id.login2);
    }

    public void _Listener() {
        login.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        String fullNameText = Objects.requireNonNull(fullName.getText()).toString();
        String phoneText = Objects.requireNonNull(phone.getText()).toString();
        String emailText = Objects.requireNonNull(email.getText()).toString();
        String passwordSingUpText = Objects.requireNonNull(passwordSingUp.getText()).toString();
        String passwordSingUpText2 = Objects.requireNonNull(passwordSingUp2.getText()).toString();
        if (fullNameText.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Full Name Cant Be Empty !", Toast.LENGTH_SHORT).show();
            fullName.setError("Can't be empty");
        } else if (phoneText.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Phone Cant Be Empty !", Toast.LENGTH_SHORT).show();
            phone.setError("Can't be empty");
        } else if (emailText.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Email Cant Be Empty !", Toast.LENGTH_SHORT).show();
            email.setError("Can't be empty");
        } else if (passwordSingUpText.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
            passwordSingUp.setError("Can't be empty");
        } else if (passwordSingUpText2.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
            passwordSingUp.setError("Can't be empty");
        } else if (!passwordSingUpText2.equals(passwordSingUpText)) {
            Toast.makeText(SignUpActivity.this, "Password Should Be The Same !", Toast.LENGTH_SHORT).show();
            passwordSingUp.setError("Can't be empty");
        } else if (passwordSingUpText.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password Must Be More Than 6 Numbers", Toast.LENGTH_SHORT).show();
            passwordSingUp.setError("Can't be less than 6 numbers");
        } else {
            progressDialog.setMessage("Sing Up...!");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(emailText, passwordSingUpText).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                String Uid = user.getUid();
                                HashMap<String, String> data = new HashMap<>();
                                data.put("uid", Uid);
                                data.put("fullName", fullNameText);
                                data.put("phone", phoneText);
                                data.put("image", "");
                                data.put("email", emailText);
                                sharedPreferences = getSharedPreferences("logged_user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_fullName", fullNameText);
                                editor.putString("user_phone", phoneText);
                                editor.putString("image", "");
                                editor.putString("user_id", Uid);
                                editor.putString("user_email", emailText);
                                editor.commit();

                                firebaseFirestore.collection("users").add(data).
                                        addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                progressDialog.dismiss();
                                                System.out.println("Created Account");
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Failed To Create Account", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                            Toast.makeText(SignUpActivity.this, "Created Account", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, HomePageActivity.class));
                            Notification();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Filed To Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    public void Notification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        //create Notification channel
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel
                    ("id", "Notifications Test ", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //send Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SignUpActivity.this, "id");

        builder.setContentTitle("Add Task ?");
        builder.setContentText("Add a task by pressing the Add to bottom button .");

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Add a task by pressing the Add to bottom button ."));
        //icon
        builder.setSmallIcon(R.drawable.ic_baseline_add_24);
        notificationManager.notify(3, builder.build());

        // btn Notification open Activity
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(SignUpActivity.this, 1021, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(3, builder.build());
    }


}