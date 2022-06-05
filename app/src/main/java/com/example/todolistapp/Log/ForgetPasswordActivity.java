package com.example.todolistapp.Log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolistapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText forgetPasswordEditText;
    Button next;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        forgetPasswordEditText = findViewById(R.id.forgetPasswordEditText);
        next = findViewById(R.id.Next_FP);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading... ");


        next.setOnClickListener(v -> {
            String email = forgetPasswordEditText.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ForgetPasswordActivity.this, "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
                forgetPasswordEditText.setError("Can't be empty");
            } else {
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        startActivity(new Intent(ForgetPasswordActivity.this,Log_in_upActivity.class));
                        finish();
                        Toast.makeText(ForgetPasswordActivity.this, "Please Check your email Address", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this, "Enter Correct email ", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            }
        });



    }
}