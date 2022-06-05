package com.example.todolistapp.Log;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import com.example.todolistapp.HomePageActivity;
import com.example.todolistapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Log_in_upActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView singUp,forgetPassword;
    TextInputEditText username, passwordLogin;
    AppCompatButton login;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        _FindId();
        _Listener();

    }

    public void _FindId() {
        singUp = findViewById(R.id.SingUp);
        username = findViewById(R.id.username);
        passwordLogin = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.loginto);
        forgetPassword = findViewById(R.id.forgetPassword);

    }

    public void _Listener() {
        singUp.setOnClickListener(v -> {
            Intent intent = new Intent(Log_in_upActivity.this,SignUpActivity.class);
            startActivity(intent);

        });

        login.setOnClickListener(this::onClick);
        forgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(Log_in_upActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void onClick(View view) {
        String usernameText = Objects.requireNonNull(username.getText()).toString();
        String passwordText = Objects.requireNonNull(passwordLogin.getText()).toString();

        if (usernameText.isEmpty()) {

            Toast.makeText(Log_in_upActivity.this, "Username Cant Be Empty !", Toast.LENGTH_SHORT).show();

            username.setError("Can't be empty");

        } else if (passwordText.isEmpty()) {
            Toast.makeText(Log_in_upActivity.this, "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
            passwordLogin.setError("Can't be empty");
        } else if (passwordText.length() < 6) {

            Toast.makeText(Log_in_upActivity.this, "Password Must Be More Than 6 Numbers", Toast.LENGTH_SHORT).show();
            passwordLogin.setError("Can't be less than 6 numbers");

        } else {
            progressDialog.setMessage("Login In...!");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(usernameText, passwordText).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Log_in_upActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onComplete: successful");
                            sharedPreferences =getSharedPreferences("logged_user",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", firebaseAuth.getCurrentUser().getUid());
                            editor.putString("user_email", usernameText);
                            editor.putString("user_fullName", firebaseAuth.getCurrentUser().getDisplayName());
                            editor.putString("user_phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                            editor.commit();  String user_id = sharedPreferences.getString("user_id", "");
                            firestore.collection("users").addSnapshotListener((value, error) -> {
                                if(error !=null){
                                    Log.d("TAG", "onEvent: error");
                                }
                                for (DocumentChange documentChange : value.getDocumentChanges()){

                                    String fullName1 = documentChange.getDocument().getData().get("DisplayName").toString();
                                    String phoneNumber1 = documentChange.getDocument().getData().get("PhoneNumber").toString();
                                    Log.d("TAG", "onEvent: "+ fullName1 +"  "+ phoneNumber1);
                                    editor.putString("user_fullName", fullName1);
                                    editor.putString("user_phone", phoneNumber1);
                                    editor.putString("user_id", firebaseAuth.getCurrentUser().getUid());
                                    editor.putString("user_email", usernameText);
                                    editor.apply();

                                }
                            });

                            startActivity(new Intent(Log_in_upActivity.this, HomePageActivity.class));
                            Notification();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Log_in_upActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onComplete: Failed");
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Log_in_upActivity.this, "id");

        builder.setContentTitle("Add Task ?");
        builder.setContentText("Add a task by pressing the Add to bottom button .");

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Add a task by pressing the Add to bottom button ."));
        //icon
        builder.setSmallIcon(R.drawable.ic_baseline_add_24);
        notificationManager.notify(3, builder.build());

        // btn Notification open Activity
        Intent intent = new Intent(Log_in_upActivity.this, HomePageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Log_in_upActivity.this, 1021, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(3, builder.build());
    }
}